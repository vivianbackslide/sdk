package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.model.VivoCreatePayDataModel;
import com.ftx.sdk.model.VivoPayModel;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.security.MD5Util;
import com.ftx.sdk.utils.vivo.VivoSignUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zeta.cai on 2016/7/18.
 */
@RestController
public class VivoPayController {
    private static final String CREATE_PAY_DATA_URL = "https://pay.vivo.com.cn/vcoin/trade";
    private Logger logger = LoggerFactory.getLogger(VivoPayController.class);

    @Autowired
    private Gson gson;
    @Autowired
    private JsonParser jsonParser;
    @Reference(version = DubboConstant.VERSION,check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private OrderService orderService;

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @RequestMapping(value = "charge/vivo")
    public String callback(@ModelAttribute VivoPayModel payInfo) {

        logger.debug("vivo charge data:{}", gson.toJson(payInfo));
        logger.info("vivo charge data:{}", gson.toJson(payInfo));
        // 获取订单
        TSdkOrder charge = orderService.queueOrder(Long.parseLong(payInfo.getCpOrderNumber()));
        if (null == charge) {
            logger.error("vivo_pay_callback接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", gson.toJson(payInfo), payInfo.getCpOrderNumber());
            return FAILURE;
        }
        //更新订单参数
        charge.setChannelBillNum(payInfo.getOrderNumber());

        //校验
        SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
        String jsonObj = gson.toJson(payInfo);
        Map<String, String> para = gson.fromJson(jsonObj, new TypeToken<Map<String, String>>() {
        }.getType());
        para.remove("signMethod");
        para.remove("signature");
        String sign = VivoSignUtils.getVivoSign(para, paramCache.channelConfig().get("channelAppKey"));

        if (charge.getAmount() != Integer.valueOf(payInfo.getOrderAmount())) {    //判断订单金额相符，若渠道没返回金额则跳过
            orderService.illegalAmountHandler(charge);
            logger.error("vivo_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), gson.toJson(payInfo));
            return SUCCESS;
        }
        if (!sign.equals(payInfo.getSignature())) {
            logger.error("vivo_pay_callback接口异常: [验签失败, requestBody={}, error_message={}]", gson.toJson(payInfo));
            return FAILURE;
        } else {
            try {
                logger.info("vivo订单成功");

                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);

                return SUCCESS;
            } catch (Exception e) {
                logger.error("vivo_pay_callback接口异常: [服务器异常:{}]", e.getMessage(), e);
                return FAILURE;
            }
        }
    }

    @RequestMapping(value = "createPayData/vivo")
    public String createPayData(@ModelAttribute VivoCreatePayDataModel data) {
        String message = "";
        String orderId = "";
        String accessKey = "";

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            SdkParamCache paramCache = sdkService.getConfig(data.getPackageId());
            String cpId = paramCache.channelConfig().get("channelCpId");
            String appId = paramCache.channelConfig().get("channelAppId");
            String appKey = paramCache.channelConfig().get("channelAppKey");
            String notifyUrl = paramCache.channelConfig().get("notifyUrl");
            do {
                if (!data.verify(appId)) {
                    message = "verify failed";
                    break;
                }

                TreeMap<String, String> map = new TreeMap<String, String>();
                map.put("version", "1.0.0");
                map.put("cpId", cpId);
                map.put("appId", appId);
                map.put("cpOrderNumber", data.getCpOrderId());
                map.put("notifyUrl", notifyUrl);
                map.put("orderTime", String.valueOf(dateformat.format(System.currentTimeMillis())));
                map.put("orderAmount", data.getTotalPrice());
                map.put("orderTitle", data.getProductName());
                map.put("orderDesc", data.getProductDes());
                String signText = MapsUtils.createLinkString(map, true, false) + "&" + MD5Util.getMD5(appKey);
                map.put("signature", MD5Util.getMD5(signText));
                map.put("extInfo", "");
                map.put("signMethod", "MD5");

                logger.debug("signText:" + signText);

                String response = HttpTools.doPost(CREATE_PAY_DATA_URL, map);
                if (StringUtils.isEmpty(response)) {
                    message = "vivo server no response";
                    break;
                }

                JsonObject result = jsonParser.parse(response).getAsJsonObject();
                int code = result.get("respCode").getAsInt();
                if (code == 200) {
                    orderId = result.get("orderNumber").getAsString();
                    accessKey = result.get("accessKey").getAsString();
                } else {
                    message = result.get("respMsg").getAsString();
                }
            } while (false);
        } catch (Exception e) {
            logger.error("vivo create pay data exception:" + e.toString());
            message = StringUtils.isEmpty(e.getMessage()) ? "unknown error" : e.getMessage();
        }

        JsonObject payData = new JsonObject();
        payData.addProperty("msg", message);
        payData.addProperty("orderId", orderId);
        payData.addProperty("accessKey", accessKey);
        return payData.toString();
    }
}
