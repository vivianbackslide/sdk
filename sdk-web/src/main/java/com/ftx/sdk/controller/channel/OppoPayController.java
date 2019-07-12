package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.model.OppoPayModel;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by zeta.cai on 2016/7/11.
 */

@RestController
public class OppoPayController {
    private Logger logger = LoggerFactory.getLogger(OppoPayController.class);

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;

    private static final String SUCCESS = "OK";
    private static final String FAILURE = "FAIL";

    @RequestMapping(value = "/charge/oppo")
    public String callback(@ModelAttribute OppoPayModel payInfo) {

        logger.debug("oppo charge data:{}", gson.toJson(payInfo));


        // 获取订单
        TSdkOrder charge = orderService.queueOrder(Long.parseLong(payInfo.getPartnerOrder()));
        if (null == charge) {
            logger.error("oppo_pay_callback接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", gson.toJson(payInfo), payInfo.getPartnerOrder());
            return result(FAILURE, "获取缓存数据失败");
        }

        //更新订单参数
        charge.setChannelBillNum(payInfo.getNotifyId());
        //校验参数
        SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
        if (!doCheck(getKebiContentString(payInfo), payInfo.getSign(), paramCache.channelAPI().get("publicKey"))) {
            logger.error("oppo_pay_callback接口异常: [验签失败, requestBody={}, error_message={}]", gson.toJson(payInfo));
            return result(FAILURE, "验签失败");
        } else if (charge.getAmount() != payInfo.getPrice()) {    //判断订单金额相符，若渠道没返回金额则跳过
            orderService.illegalAmountHandler(charge);
            logger.error("oppo_pay_callback 接口异常: [订单金额异常: order:{}, channelOrder:{}]",
                    gson.toJson(charge), payInfo.getNotifyId());
            return result(SUCCESS, null);
        } else {

            logger.info("oppo订单成功");
            //向游戏异步发送请求

            //执行统一逻辑，包括入库和通知游戏
            callbackService.orderHandler(charge, paramCache);

            return result(SUCCESS, null);
        }
    }

    private String result(String result, String resultMsg) {
        return "result=" + result + "&" + "resultMsg=" + resultMsg;
    }

    private String getKebiContentString(OppoPayModel payInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("notifyId=").append(payInfo.getNotifyId());
        sb.append("&partnerOrder=").append(payInfo.getPartnerOrder());
        sb.append("&productName=").append(payInfo.getProductName());
        sb.append("&productDesc=").append(payInfo.getProductDesc());
        sb.append("&price=").append(payInfo.getPrice());
        sb.append("&count=").append(payInfo.getCount());
        sb.append("&attach=").append(payInfo.getAttach());
        return sb.toString();
    }

    public boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new
                    X509EncodedKeySpec(encodedKey));
            java.security.Signature signature =
                    java.security.Signature.getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            boolean bverify = signature.verify(Base64.decodeBase64(sign));
            return bverify;
        } catch (Exception e) {
            logger.error("验证签名出错.");
        }
        return false;
    }
}
