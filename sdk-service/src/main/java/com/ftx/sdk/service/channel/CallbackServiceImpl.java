package com.ftx.sdk.service.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.order.CallbackModel;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.type.ChargeStatus;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.VerifyUitl;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zeta.cai on 2017/8/8.
 */
@Service
@org.apache.dubbo.config.annotation.Service(version = DubboConstant.VERSION)
public class CallbackServiceImpl implements CallbackService {

    @Autowired
    private SDKService sdkService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CuratorFramework client;
   /* @Autowired
    private RedisService redisService;*/
    /*@Autowired
    private Gson gson;*/

    private static final Logger logger = LoggerFactory.getLogger(CallbackServiceImpl.class);
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    @Override
    public void orderHandler(TSdkOrder charge, SdkParamCache paramCache) {
        // 只有第一次支付回调，走这里
        if (charge.getStatus() != 0) {
            logger.error("调用发货失败: [已处理的订单, orderId={}]", charge.getOrderId());
            return;
        }

        //渠道单号为空则填充
        if (Strings.isNullOrEmpty(charge.getChannelBillNum())) {
            charge.setChannelBillNum("fake_" + charge.getOrderId());
        }

        addToPool(charge, paramCache);
    }

    @Override
    public void addToPool(TSdkOrder charge, SdkParamCache paramCache) {
        // 补单，可以走这里，status可能是任意值
        //配置回调地址
        String notifyUrl = charge.getNotifyUrl();
        if (Strings.isNullOrEmpty(notifyUrl)) {
            notifyUrl = paramCache.getAppPayServer();
        }
        if (Strings.isNullOrEmpty(notifyUrl)) {
            logger.error("调用发货接口失败:[未配置发货接口, package_id={}]", paramCache.getPackageId());
            return;
        }
        notifyUrl = HttpTools.urlPadding(notifyUrl);

        //创建支付回调参数对象
        CallbackModel callbackInfo = new CallbackModel(charge);
        callbackInfo.setSign(VerifyUitl.createCallbackSign(callbackInfo, paramCache.getAppSecret()));
        //加入线程池
        EXECUTOR_SERVICE.submit(new Notifier(callbackInfo, notifyUrl));
    }

    /*private void pushNotification(Notifier notifier) {
        redisService.pushNotifyCache(gson.toJson(notifier));
    }*/

    //  定时任务，每隔1分钟从redis中取出失败订单检查是否需要通知
    @Scheduled(cron = "0 */1 * * * *")
    public void reNotify() {
        //List<String> list = redisService.popNotifyCache();
        List<TSdkOrder> tSdkOrders = orderService.queryOrderByStatus(ChargeStatus.PaySuccessNotifyFailed);

        for (TSdkOrder order : tSdkOrders) {
            String notifyUrl = order.getNotifyUrl();

            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(order.getPackageId()));
            if (StringUtils.isEmpty(notifyUrl)) {
                notifyUrl = paramCache.getAppPayServer();
            }

            if (StringUtils.isEmpty(notifyUrl)) {
                logger.error("调用发货接口失败:[未配置发货接口, package_id={}]", paramCache.getPackageId());
                return;
            }

            notifyUrl = HttpTools.urlPadding(notifyUrl);

            CallbackModel callbackInfo = new CallbackModel(order);
            callbackInfo.setSign(VerifyUitl.createCallbackSign(callbackInfo, paramCache.getAppSecret()));
            Notifier notifier = new Notifier(callbackInfo, notifyUrl);
           /* if (ReNotifierTimeInterval.isExistTimeInterval(notifier.count)) {     //是否需要继续通知
                //设置订单状态通知失败
                orderService.updateStatus(Long.parseLong((notifier.callbackInfo.getPlatformBillNo())), ChargeStatus.NotifyFailed);
            } else if (ReNotifierTimeInterval.needReNotify(notifier.beginTimeMillis, notifier.count)) {  */        //当前时间超过这次通知的时间片  执行通知
            EXECUTOR_SERVICE.submit(new Notifier(notifier));
           /* } else {         //当前时间小于这次通知的时间片  不通知  将该task放回通知队列
                // pushNotification(notifier);
            }*/

        }


        /*for (String notifierJson : list) {
            Notifier notifier = gson.fromJson(notifierJson, Notifier.class);
            if (ReNotifierTimeInterval.isExistTimeInterval(notifier.count)) {     //是否需要继续通知
                //设置订单状态通知失败
                orderService.updateStatus(Long.parseLong((notifier.callbackInfo.getPlatformBillNo())), ChargeStatus.NotifyFailed);
            } else if (ReNotifierTimeInterval.needReNotify(notifier.beginTimeMillis, notifier.count)) {          //当前时间超过这次通知的时间片  执行通知
                EXECUTOR_SERVICE.submit(new Notifier(notifier));
            } else {         //当前时间小于这次通知的时间片  不通知  将该task放回通知队列
                // pushNotification(notifier);
            }
        }*/
    }

    private class Notifier implements Runnable {
        private CallbackModel callbackInfo;
        private String notifyUrl;
        private Long beginTimeMillis;
        //private int count;

        Notifier(CallbackModel callbackInfo, String notifyUrl) {
            this.callbackInfo = callbackInfo;
            this.notifyUrl = notifyUrl;
            //this.count = 0;
        }

        Notifier(Notifier notifier) {
            this.callbackInfo = notifier.callbackInfo;
            this.notifyUrl = notifier.notifyUrl;
            this.beginTimeMillis = notifier.beginTimeMillis;
            //this.count = notifier.count;
        }


        @Override
        public void run() {
            if (Strings.isNullOrEmpty(notifyUrl)) {
                logger.error("调用发货接口失败:[未配置发货接口, package_id={}]", callbackInfo.getPackageId());
                return;
            }
            String path = "/distributed-lock/" + callbackInfo.getPlatformBillNo();
            InterProcessLock lock = new InterProcessMutex(client, path);
            try {
                lock.acquire();
                TSdkOrder charge = orderService.queueOrder(Long.valueOf(callbackInfo.getPlatformBillNo()));
                if (ChargeStatus.PaySuccessNotifySuccess.getType() == charge.getStatus()) {
                    logger.info("订单号:" + charge.getOrderId() + "已完成,无需重复发货");
                    return;
                }
                String result = HttpTools.doPost(notifyUrl, MapsUtils.object2StringMap(callbackInfo));
                logger.info("已通知游戏发货:[URL={}, callbackInfo={}]", notifyUrl, callbackInfo.toString());

                //更新通知次数
                //scount++;

                if (null == result || !"success".equalsIgnoreCase(result.trim())) {
                    logger.info("调用发货接口失败:[URL={}, package_id={}, result={}]", notifyUrl, callbackInfo.getPackageId(), result);
                    handleFailed();
                } else {
                    logger.info("调用发货接口成功:[URL={}, package_id={}]", notifyUrl, callbackInfo.getPackageId());
                    handleSucceed();
                }
            } catch (Exception e) {
                logger.error("Notifier run error", e.getMessage());
            } finally {
                try {
                    lock.release();
                    Stat stat = client.checkExists().forPath(path);
                    List<String> children = client.getChildren().forPath(path);
                    if (null != stat && CollectionUtils.isEmpty(children)) {
                        client.delete().guaranteed().forPath(path);
                    }
                } catch (Exception e) {
                    logger.error("Notifier run error", e.getMessage());
                }
            }

        }


        //订单要在收到回调的时候才获取到渠道订单id，因此在更新订单状态的同时更新渠道单号
        private void handleSucceed() {
            orderService.orderComplete(Long.parseLong(callbackInfo.getPlatformBillNo()), callbackInfo.getChannelBillNo());
        }

        private void handleFailed() {
            /* if (1 == count) {*/
            orderService.orderUpdateOnFirstCallback(Long.parseLong(callbackInfo.getPlatformBillNo()), callbackInfo.getChannelBillNo(), ChargeStatus.PaySuccessNotifyFailed);
            //beginTimeMillis = System.currentTimeMillis();
            /* }*/
            //pushNotification(this);
        }
    }
}
