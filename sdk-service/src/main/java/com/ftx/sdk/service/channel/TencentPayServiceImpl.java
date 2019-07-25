package com.ftx.sdk.service.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.type.ChargeStatus;
import com.ftx.sdk.model.TencentInterfaceType;
import com.ftx.sdk.model.TencentPayModel;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.tencent.TencentUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@org.apache.dubbo.config.annotation.Service(version = DubboConstant.VERSION)
public class TencentPayServiceImpl implements TencentPayService {

    private Logger logger = LoggerFactory.getLogger(TencentPayServiceImpl.class);

    @Autowired
    private SDKService sdkService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private Gson gson;
    @Autowired
    private JsonParser jsonParser;
    @Autowired
    private CallbackService callbackService;
    @Autowired
    private TencentPayModelService tencentPayModelService;

    private final static ExecutorService executor = Executors.newCachedThreadPool();
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    //调用腾讯查询余额接口
    @Override
    public String queryBalance(TencentPayModel payModel) {
        String orderId = payModel.getBillno();
        TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderId));
        SdkParamCache configCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
        String url = TencentUtil.createUrl(TencentInterfaceType.Query, configCache);
        Map<String, String> cookie = TencentUtil.createCookie(payModel, TencentInterfaceType.Query);
        Map<String, String> data = TencentUtil.createRequestData(payModel, TencentInterfaceType.Query, configCache);
        return HttpTools.doGetByCookie(url, data, cookie);
    }

    //调用腾讯扣款接口
    @Override
    public String deductGameCurrency(TencentPayModel payModel) {
        String orderId = payModel.getBillno();
        TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderId));

        SdkParamCache configCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
        String url = TencentUtil.createUrl(TencentInterfaceType.Pay, configCache);
        Map<String, String> cookie = TencentUtil.createCookie(payModel, TencentInterfaceType.Pay);
        Map<String, String> data = TencentUtil.createRequestData(payModel, TencentInterfaceType.Pay, configCache);
        return HttpTools.doGetByCookie(url, data, cookie);
    }

    @Override
    public void pay(TencentPayModel payModel) {
        EXECUTOR_SERVICE.submit(new TencentOrderTrace(payModel));
    }

    private class TencentOrderTrace implements Runnable {

        private TencentPayModel payModel;
        //private int count;

        public TencentOrderTrace(TencentPayModel tencentPayModel) {
            this.payModel = tencentPayModel;
            //this.count = 0;
        }

        public TencentOrderTrace(TencentOrderTrace notifier) {
            this.payModel = notifier.payModel;
            //  this.count = notifier.count;
        }

        @Override
        public void run() {
            // ++count;

            //从数据库中获取订单
            String orderId = payModel.getBillno();
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderId));
            if (charge.getStatus() != ChargeStatus.NewOrder.getType())
                return;

            //查询余额
            String response = queryBalance(payModel);
            JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
            int code = jsonObject.get("ret").getAsInt();
            if (code != 0) {
                logger.info("tecnent pay 查询余额失败");
                handleFailed();
                return;
            }

            long balance = jsonObject.get("balance").getAsLong();
            if (balance < payModel.getAmt()) {
                logger.info("tecnent pay 用户账户余额不足:[{}] payModel={}, balance={}", TencentUtil.getMessageByCode(code), payModel.toString(), balance);
                handleFailed();
                return;
            }

            //开始支付
            response = deductGameCurrency(payModel);
            jsonObject = jsonParser.parse(response).getAsJsonObject();
            code = jsonObject.get("ret").getAsInt();
            if (code != 0) {
                logger.info("tecnent pay 扣款失败:[{}] payModel={}, balance={}", TencentUtil.getMessageByCode(code), payModel.toString(), balance);
                handleFailed();
                return;
            }
            logger.info("tecnent pay 扣款成功:[{}] payModel={}, balance={}", TencentUtil.getMessageByCode(code), payModel.toString(), jsonObject.get("balance"));
            handleSucceed();
        }

        private void handleSucceed() {
            notifyGame(payModel);
        }

        private void handleFailed() {
            /*if (count <= 8){
                addTask(this);
            }*/
            tencentPayModelService.insert(payModel);
        }
    }

   /* private void addTask(TencentOrderTrace tencentOrderTrace) {
        taskService.push(gson.toJson(tencentOrderTrace));
    }*/

    //  定时任务，每隔15秒从redis中取出失败订单检查是否需要通知
    @Scheduled(cron = "*/15 * * * * *")
    public void task() {
        //List<String> list = taskService.popNotifyCache();
        List<TencentPayModel> payModel2DeleteList = new ArrayList<>();
        List<TencentPayModel> modelList = tencentPayModelService.getAll();
        if (CollectionUtils.isEmpty(modelList)) {
            return;
        }
        for (TencentPayModel payModel : modelList) {
            //TencentOrderTrace notifier = gson.fromJson(notifierJson, TencentOrderTrace.class);
            TSdkOrder tSdkOrder = orderService.queueOrder(Long.valueOf(payModel.getBillno()));
            if (ChargeStatus.PaySuccessNotifySuccess.getType() == tSdkOrder.getStatus()) {
                payModel2DeleteList.add(payModel);
            } else {
                EXECUTOR_SERVICE.submit(new TencentOrderTrace(payModel));
            }
        }
        if (CollectionUtils.isNotEmpty(payModel2DeleteList)) {
            tencentPayModelService.batchDelete(payModel2DeleteList);
        }
    }

    private void notifyGame(TencentPayModel tencentPayModel) {
        // 2.获取存在缓存的透传（自定义）参数
        String orderId = tencentPayModel.getBillno();
        TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderId));
        if (null == charge) {
            logger.error("tencent_pay_callback接口异常: [获取订单数据失败, requestBody={}, orderId={}]", gson.toJson(tencentPayModel), orderId);
            return;
        }

        //更新订单参数
        charge.setChannelBillNum(tencentPayModel.getBillno());

        //执行统一逻辑，包括入库和通知游戏
        SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
        callbackService.orderHandler(charge, paramCache);
    }
}
