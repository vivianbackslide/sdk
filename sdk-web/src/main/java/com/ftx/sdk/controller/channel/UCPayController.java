package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.UCPayModel;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.service.channel.UCService;
import com.ftx.sdk.utils.MoneyUtil;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UCPayController {

    private final static String SUCCESS = "SUCCESS";
    private final static String FAILUER = "FAILUER";

    private Logger logger = LoggerFactory.getLogger(UCPayController.class);

    @Autowired
    private Gson gson;
    @Autowired
    private UCService ucService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private OrderService orderService;

    @RequestMapping(value = "/charge/uc")
    public String callback(HttpServletRequest request) {
        String requestBody = "";
        ServletInputStream stream = null;
        try {
            stream = request.getInputStream();
            requestBody = IOUtils.toString(stream, "UTF-8");
            logger.debug("uc charge data:{}", requestBody);
            // 1.检查字符串参数是否为空
            if (Strings.isNullOrEmpty(requestBody)) {
                logger.error("uc_pay_callback接口异常: [requestBody = null]");
                return FAILUER;
            }
            UCPayModel ucPayModel = gson.fromJson(requestBody, UCPayModel.class);
            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(ucPayModel.getData().getCpOrderId()));
            if(null == charge) {
                logger.error("uc_pay_callback接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", requestBody, ucPayModel.getData().getCpOrderId());
                return FAILUER;
            }

            //更新订单参数
            charge.setChannelBillNum(ucPayModel.getData().getOrderId());

            //校验
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            JsonResult<?> payResult = ucService.pay(ucPayModel, paramCache);

            if (charge.getAmount() != MoneyUtil.yuan2fen(ucPayModel.getData().getAmount())) {    //判断订单金额相符，若渠道没返回金额则跳过
                orderService.illegalAmountHandler(charge);
                logger.error("uc_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), gson.toJson(ucPayModel));
                return SUCCESS;
            }

            // 7.判断回调验证是否成功，成功则进行异步发货
            if (payResult.success()) {

                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);
                return SUCCESS;
            } else {
                orderService.VerifyFailed(charge);
                logger.error("uc_pay_callback接口异常:[requestData={}, errorMessage={}]", requestBody, payResult.getMessage());
                return FAILUER;
            }
        } catch (Exception e) {
            logger.error("uc_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", requestBody, e.getMessage(), e);
            return FAILUER;
        }finally {
            try {
                if (null != stream ) {
                    stream.close();
                }
            } catch (Exception e) {
                logger.error("uc_pay_callback接口异常 stream close error : {}", e.getMessage(), e);
            }
        }
    }
}
