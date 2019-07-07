package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.BaiduPayModel;
import com.ftx.sdk.service.channel.BaiduService;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.MoneyUtil;
import com.ftx.sdk.utils.security.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lei.nie on 2016/7/9.
 */

@RestController
public class BaiduPayController {
    private Logger logger = LoggerFactory.getLogger(BaiduPayController.class);
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

    @Autowired
    private JsonParser jsonParser;

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;
    @Autowired
    private BaiduService baiduService;

    @RequestMapping(value = "/charge/bd")
    public String callback(@ModelAttribute BaiduPayModel baiduPayModel) {
        try {
            logger.debug("baidu charge data:{}", gson.toJson(baiduPayModel));

            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(baiduPayModel.getCooperatorOrderSerial()));
            if (null == charge) {
                logger.error("baidu_pay_callback接口异常: [获取订单数据失败, requestBody={}, orderId={}]", gson.toJson(baiduPayModel), baiduPayModel.getCooperatorOrderSerial());
                return FAILED;
            }

            //更新订单参数
            charge.setChannelBillNum(baiduPayModel.getOrderSerial());

            // 4.调用服务方法进行支付
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            JsonResult<?> payResult = baiduService.pay(baiduPayModel, paramCache);

            byte[] contentBytes = Base64.decode(baiduPayModel.getContent());
            String content = new String(contentBytes);
            JsonObject jsonObject = jsonParser.parse(content).getAsJsonObject();
            double money = jsonObject.get("OrderMoney").getAsDouble();

            String response = baiduService.createResponse(baiduPayModel, paramCache);
            // 6.判断回调验证是否成功，成功则进行异步发货
            if (!payResult.success()) {
                orderService.VerifyFailed(charge);
                logger.error("baidu_pay_callback接口异常:[签名校验失败, requestData={}, errorMessage={}]", gson.toJson(baiduPayModel), payResult.getMessage());
                return response;
            } else if (charge.getAmount() != MoneyUtil.yuan2fen(money)) {    //判断订单金额相符，若渠道没返回金额则跳过
                orderService.illegalAmountHandler(charge);
                logger.error("baidu_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), jsonObject);
                return SUCCESS;
            } else {
                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);
                return response;
            }
        } catch (Exception e) {
            logger.error("baidu_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", gson.toJson(baiduPayModel), e.getMessage(), e);
            return FAILED;
        }
    }
}
