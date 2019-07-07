package com.ftx.sdk.service.channel;

import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.BaiduPayModel;
import com.ftx.sdk.utils.security.Base64;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lei.nie on 2016/7/9.
 */

@SuppressWarnings("SpellCheckingInspection")
@Service
public class BaiduService {
    private Logger logger = LoggerFactory.getLogger(BaiduService.class);

    @Autowired
    private JsonParser jsonParser;

    @SuppressWarnings("Since15")
    public JsonResult<?> pay(BaiduPayModel payModel, SdkParamCache paramCache) {
        String appSecret  = paramCache.channelConfig().get("channelAppSecret");
        if (Strings.isNullOrEmpty(appSecret)) {
            return new JsonResult<>(ErrorCode.ServerConfError.APP_CHANNEL_CONF_SECRET_NULL.getCode(), "appSecret is null");
        }
        try {
            String sign = payModel.getSign();
            byte[] contentBytes = Base64.decode(payModel.getContent());
            String content = new String(contentBytes);
            JsonObject jsonObject = jsonParser.parse(content).getAsJsonObject();
            int orderStatus = jsonObject.get("OrderStatus").getAsInt();
            if(orderStatus == 1){
                int appId = payModel.getAppID();
                String orderSerial = payModel.getOrderSerial();
                String cpOrder = payModel.getCooperatorOrderSerial();

                String base64Content1 = payModel.getContent();
                String base64Content2 = new String(Base64.encode(content.getBytes("UTF-8")));
                String checkSign = MD5Util.getMD5(appId + orderSerial + cpOrder + base64Content1 + appSecret);
                if(sign.equals(checkSign)){
                    double money = jsonObject.get("OrderMoney").getAsDouble();
                    return new JsonResult<Object>(ErrorCode.Success.SUCCESS.getCode(), "success");
                }
                else{
                    return new JsonResult<Object>(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode(), "sign error");
                }
            }
            else{
                return new JsonResult<Object>(ErrorCode.RequestError.REQUEST_ORDER_FAILED.getCode(), "order not success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("baidu_pay_callback计算签名时异常:{}", e.getMessage());
            return new JsonResult<Object>(ErrorCode.ServerError.SERVER_ERROR.getCode(), "make sign error");
        }
    }

    @SuppressWarnings("Since15")
    public String createResponse(BaiduPayModel payModel, SdkParamCache paramCache){
        String appSecret  = paramCache.channelConfig().get("channelAppSecret");

        int appId = payModel.getAppID();
        byte[] contentBytes = Base64.decode(payModel.getContent());
        String content = new String(contentBytes);
        JsonObject jsonObject = jsonParser.parse(content).getAsJsonObject();
        int resultCode = jsonObject.get("OrderStatus").getAsInt();
        String resultMsg = jsonObject.get("StatusMsg").getAsString();
        String sign = MD5Util.getMD5(appId + resultCode + appSecret);

        jsonObject = new JsonObject();
        jsonObject.addProperty("AppID", appId);
        jsonObject.addProperty("ResultCode", resultCode);
        jsonObject.addProperty("ResultMsg", resultMsg);
        jsonObject.addProperty("Sign", sign);
        jsonObject.addProperty("Content", "");
        String response = jsonObject.toString();
        return response;
    }
}
