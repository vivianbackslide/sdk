package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by runmin.han on 2019/6/4.
 */
@RestController
public class QuickGamePayController {
    private Logger logger = LoggerFactory.getLogger(QuickGamePayController.class);

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILURE";

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;

    @RequestMapping(value = "/charge/QuickGame")
    public String callback(@RequestParam Map<String,String> quickGamePayModel){
        try {
            logger.debug("QuickGame charge data:{}", gson.toJson(quickGamePayModel));
            logger.info("QuickGame charge data:{}", gson.toJson(quickGamePayModel));
            //获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(quickGamePayModel.get("cpOrderNo")));
            if (null == charge) {
                logger.error("QuickGame_pay_callback接口异常: [获取订单数据失败, requestBody={}, orderId={}]", gson.toJson(quickGamePayModel), quickGamePayModel.get("cpOrderNo"));
                return FAILED;
            }

            //更新订单参数(channel_bill_num加入SDK订单号)
            charge.setChannelBillNum(quickGamePayModel.get("orderNo"));
            //校验签名
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            String sign = quickGamePayModel.get("sign");

            String callbackKey = paramCache.channelConfig().get("callbackKey");
//            Map<String, String> map = MapsUtils.object2StringMap(quickGamePayModel);
            quickGamePayModel.remove("sign");

            String signStr = MapsUtils.createLinkString(quickGamePayModel, true, false);//排列成url键值对格式进行签名
            signStr = signStr + "&" + callbackKey;
            String mySign = MD5Util.getMD5(signStr);

            // 金额校验(由于港澳台sdk用的是美金，所以暂时无法校验需要等游戏更改商品价格)
//            if (Double.valueOf(charge.getAmount()) / 100 != Double.valueOf(quickGamePayModel.getParameter("usdAmount"))) {    //判断订单金额相符，若渠道没返回金额则跳过
//                orderService.illegalAmountHandler(charge);
//                logger.error("QuickGame_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), gson.toJson(quickGamePayModel));
//                return FAILED;
//            }

            // 判断回调验证是否成功，成功则进行异步发货
            //比较游戏传来的sign和自己拼接传来的sign是否相同，相同则表示没有被修改，可以发货
            if (sign.equalsIgnoreCase(mySign)) {
                //执行统一逻辑，包括入库通知游戏
                callbackService.orderHandler(charge, paramCache);
                logger.info("QuickGame_pay_callback 支付成功!");
                return SUCCESS;
            } else {
                logger.error("QuickGame_pay_callback接口签名校验异常: QuickGameSign:{}, mySign:{}, MysignStr:{}]", sign, mySign, signStr);
                return FAILED;
            }
        }catch (Exception e){
            logger.error("QuickGame_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", gson.toJson(quickGamePayModel), e.getMessage(), e);
            return FAILED;
        }
    }
}
