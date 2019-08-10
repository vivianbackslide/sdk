package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.YijiePayModel;
import com.ftx.sdk.service.channel.YijieService;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhenbiao.cai
 * @date 2016/8/26.
 */
@RestController
public class YijiePayController {

    private Logger logger = LoggerFactory.getLogger(YijiePayController.class);

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION,check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private OrderService orderService;
    @Autowired
    private YijieService yijieService;

    @RequestMapping(value = "/charge/yijie")
    public String callback(@ModelAttribute YijiePayModel yijiePayModel) {
        try {
            logger.debug("yijie charge data:{}", gson.toJson(yijiePayModel));
            logger.info("yijie charge data:{}", gson.toJson(yijiePayModel));
            // 先对易接订单进行分解
            String orderId = yijiePayModel.getCbi();
            String packageId = "";
            String[] orderIdAndPackageId = orderId.split("_");
            if (orderIdAndPackageId.length == 2) {
                orderId = orderIdAndPackageId[0];
                // 这个packageId的channelId不是易接的，即不是112
                packageId = orderIdAndPackageId[1];
            }

            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderId));
            if (null == charge) {
                logger.error("yijie_pay_callback接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", gson.toJson(yijiePayModel), yijiePayModel.getSsid());
                return FAILED;
            }
            String yijiePackageId = charge.getPackageId();

            //判断订单金额相符，若渠道没返回金额则跳过
            if (charge.getAmount() != Integer.valueOf(yijiePayModel.getFee())) {
                orderService.illegalAmountHandler(charge);
                logger.error("yijie_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), gson.toJson(yijiePayModel));
                return SUCCESS;
            }

            // 4.调用服务方法进行支付
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(yijiePackageId));
            JsonResult<?> payResult = yijieService.pay(yijiePayModel, paramCache);
            if (payResult.success()) {

                //更新订单参数，针对易接替换掉packageId
                if (!Strings.isNullOrEmpty(packageId))
                    charge.setPackageId(packageId);
                charge.setChannelBillNum(yijiePayModel.getTcd());
                orderService.update(charge);

                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);
                return SUCCESS;
            } else {
                logger.error("yijie_pay_callback接口异常:[签名校验失败, requestData={}, errorMessage={}]", gson.toJson(yijiePayModel), payResult.getMessage());
            }
        } catch (Exception e) {
            logger.error("yijie_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", gson.toJson(yijiePayModel), e.getMessage(), e);
            return FAILED;
        }
        return FAILED;
    }
}
