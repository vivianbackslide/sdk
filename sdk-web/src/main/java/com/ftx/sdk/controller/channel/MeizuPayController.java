package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.MeizuPayModel;
import com.ftx.sdk.model.MeizuResult;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.MeizuService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.MoneyUtil;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jiteng.huang on 2016/7/12.
 */
@RestController
public class MeizuPayController {
    private Logger logger = LoggerFactory.getLogger(MeizuPayController.class);

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;

    @Autowired
    private MeizuService meizuService;

    @RequestMapping(value = "/charge/meizu")
    public String callback(@ModelAttribute MeizuPayModel meizuPayModel) {
        try {
            logger.debug("meizu charge data:{}", gson.toJson(meizuPayModel));
            logger.info("meizu charge data:{}", gson.toJson(meizuPayModel));
            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(meizuPayModel.getCp_order_id()));
            if(null == charge) {
                logger.error("meizu_pay_callback 接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", gson.toJson(meizuPayModel), meizuPayModel.getCp_order_id());
                return gson.toJson(new MeizuResult("120014", "", "", null));
            }
            // 4.调用服务方法进行支付
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            JsonResult<?> payResult = meizuService.check(meizuPayModel, paramCache);

            //更新订单参数
            charge.setChannelBillNum("fake_" + String.valueOf(meizuPayModel.getCp_order_id()));

            // 6.判断回调验证是否成功，成功则进行异步发货
            if (payResult.success()) {
                if (charge.getAmount() != MoneyUtil.yuan2fen(meizuPayModel.getTotal_price())) {    //判断订单金额相符，若渠道没返回金额则跳过
                    orderService.illegalAmountHandler(charge);
                    logger.error("meizu_pay_callback 接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), "fake_" + String.valueOf(meizuPayModel.getCp_order_id()));
                    return gson.toJson(new MeizuResult("200", "", "", null));//发货成功;
                }
                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);
                return gson.toJson(new MeizuResult("200", "", "", null));//发货成功
            } else {
                logger.error("meizu_pay_callback接口异常:[requestData={}, errorMessage={}]", gson.toJson(meizuPayModel), payResult.getMessage());
                return gson.toJson(new MeizuResult("120014", "", "", null));//发货失败
            }
        }catch (Exception e) {
            logger.error("meizu_pay_callback接口异常:[服务器异常: requestBody={}, error_message={}]", gson.toJson(meizuPayModel), e.getMessage(), e);
            return gson.toJson(new MeizuResult("120014", "", "", null));
        }

    }
}
