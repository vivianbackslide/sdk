package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.xiaomi.XiaoMiPayModel;
import com.ftx.sdk.model.xiaomi.XiaoMiResult;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.service.channel.XiaoMiService;
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
 * @date 2016/6/27.
 */
@SuppressWarnings("SpellCheckingInspection")
@RestController
public class XiaomiPayController {

    private Logger logger = LoggerFactory.getLogger(XiaomiPayController.class);

    @Autowired
    private Gson gson;
    @Autowired
    private XiaoMiService miService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private OrderService orderService;

    @RequestMapping(value = "/charge/xiaomi")
    public String callback(@ModelAttribute XiaoMiPayModel xiaomiPayModel) {
        try {
            logger.debug("xiaomi charge data:{}", gson.toJson(xiaomiPayModel));
            logger.info("xiaomi charge data:{}", gson.toJson(xiaomiPayModel));
            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(xiaomiPayModel.getCpOrderId()));
            if(null == charge) {
                logger.error("xiaomi_pay_callback接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", gson.toJson(xiaomiPayModel), xiaomiPayModel.getCpOrderId());
                return gson.toJson(new XiaoMiResult(1515, "cpOrderId 错误"));
            }
            //更新订单参数
            charge.setChannelBillNum(xiaomiPayModel.getOrderId());
            if (charge.getAmount() != Integer.valueOf(xiaomiPayModel.getPayFee())) {    //判断订单金额相符，若渠道没返回金额则跳过
                orderService.illegalAmountHandler(charge);
                logger.error("xiaomi_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), gson.toJson(xiaomiPayModel));
                return gson.toJson(new XiaoMiResult(200, "成功"));
            }
            // 4.调用服务方法进行支付
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            JsonResult<?> payResult = miService.pay(xiaomiPayModel, paramCache, charge);

            // 6.判断回调验证是否成功，成功则进行异步发货
            if (payResult.success()) {
                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);
                return gson.toJson(new XiaoMiResult(200, "成功"));
            } else {
                logger.error("xiaomi_pay_callback接口异常:[requestData={}, errorMessage={}]", gson.toJson(xiaomiPayModel), payResult.getMessage());
                return gson.toJson(new XiaoMiResult(1525, "signature 错误"));
            }
        } catch (Exception e) {
            logger.error("xiaomi_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", gson.toJson(xiaomiPayModel), e.getMessage(), e);
        }
        return gson.toJson(new XiaoMiResult(1525, "signature 异常"));
    }
}
