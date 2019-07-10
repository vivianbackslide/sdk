package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.type.ChargeStatus;
import com.ftx.sdk.model.MaoerPayModel;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.maoer.MapsUtils;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by runmin.han on 2018/9/17.
 */
@RestController
public class MaoerPayController {

    private Logger logger = LoggerFactory.getLogger(MaoerPayController.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String ErrorSign = "ErrorSign";
    private static final String FAILED = "FAILURE";

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;

    @RequestMapping(value = "/charge/Maoer")
    public String callback(@ModelAttribute MaoerPayModel maoerPayModel) {
        try {

            logger.debug("maoer charge data:{}", gson.toJson(maoerPayModel));
            //获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(maoerPayModel.getCallback_info()));
            if (null == charge) {
                logger.error("maoer_pay_callback接口异常: [获取订单数据失败, requestBody={}, orderId={}]", gson.toJson(maoerPayModel), maoerPayModel.getCallback_info());
                return FAILED;
            }

            //更新订单参数(channel_bill_num)
            charge.setChannelBillNum(maoerPayModel.getOrder_id());

            //校验签名
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            String sign = maoerPayModel.getSign();

            String channelAppKey = paramCache.channelConfig().get("channelAppKey");

            Map<String, String> map = MapsUtils.object2StringMap(maoerPayModel);
            map.remove("extend");
            map.remove("sign");

            //将map拼接成a=avalue#b=bvalue#.....#z=zvalue#key，因为juyou的sdk文档已说需要以参数的key名字母顺序（升序）排，所以第二个参数为true
            String signStr = MapsUtils.createLinkString(map, true, false);
            signStr = signStr + "#" + channelAppKey;
            String mySign = MD5Util.getMD5(signStr);

            // 金额校验
            if (charge.getAmount() / 100 != Integer.valueOf(maoerPayModel.getAmount())) {    //判断订单金额相符，若渠道没返回金额则跳过
                orderService.illegalAmountHandler(charge);
                logger.error("maoer_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), gson.toJson(maoerPayModel));
                return FAILED;
            }
            // 判断回调验证是否成功，成功则进行异步发货
            //比较游戏传来的sign和自己拼接传来的sign是否相同，相同则表示没有被修改，可以发货
            if (sign.equalsIgnoreCase(mySign)) {
                /** 状态是1则支付成功，否则支付失败*/
                if (1 != maoerPayModel.getStatus()) {
                    orderService.orderUpdate(charge.getOrderId(), charge.getChannelBillNum(), ChargeStatus.PayFailed);
                    return FAILED;
                }
                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);
                logger.info("maoer_pay_callback 支付成功!");
                return SUCCESS;
            } else {
                orderService.orderUpdate(charge.getOrderId(), charge.getChannelBillNum(), ChargeStatus.PayFailed);
                logger.error("maoer_pay_callback接口签名校验异常: sign:{}, mySign:{}, signStr:{}]", sign, mySign, signStr);
                return ErrorSign;
            }
        } catch (Exception e) {
            logger.error("maoer_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", gson.toJson(maoerPayModel), e.getMessage(), e);
            return FAILED;
        }

    }
}
