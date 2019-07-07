package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.QihooPayModel;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.QihooService;
import com.ftx.sdk.service.channel.SDKService;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lei.nie on 2016/7/11.
 */
@RestController
public class QihooPayController {
    private Logger logger = LoggerFactory.getLogger(QihooPayController.class);
    private static final String OK = "ok";

    @Autowired
    private Gson gson;
    @Autowired
    private QihooService qihooService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;

    @RequestMapping(value = "/charge/360")
    public String callback(@ModelAttribute QihooPayModel qihooPayModel) {
        try {
            logger.debug("360 charge data:{}", gson.toJson(qihooPayModel));

            String orderId = qihooPayModel.getApp_order_id();
            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderId));
            if (null == charge) {
                logger.error("360_pay_callback接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", gson.toJson(qihooPayModel), orderId);
                return OK;
            }

            //更新订单参数
            charge.setChannelBillNum(String.valueOf(qihooPayModel.getOrder_id()));

            //校验参数
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            JsonResult<?> payResult = qihooService.pay(qihooPayModel, paramCache);

            // 6.判断回调验证是否成功，成功则进行异步发货
            if (!payResult.success()) {
                orderService.VerifyFailed(charge);
                logger.error("360_pay_callback接口异常:[requestData={}, errorMessage={}]", gson.toJson(qihooPayModel), payResult.getMessage());
                return OK;
            } else if (charge.getAmount() != qihooPayModel.getAmount()) {    //判断订单金额相符，若渠道没返回金额则跳过
                orderService.illegalAmountHandler(charge);
                logger.error("360_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), gson.toJson(qihooPayModel));
                return OK;
            } else {
                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);
                return OK;
            }
        } catch (Exception e) {
            logger.error("360_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", gson.toJson(qihooPayModel), e.getMessage(), e);
            return OK;
        }
    }
}
