package com.ftx.sdk.service.channel;

import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.MeizuPayModel;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by jiteng.huang on 2016/7/12.
 */
@Service
public class MeizuService {
    private Logger logger = LoggerFactory.getLogger(MeizuService.class);
    @Autowired
    private Gson gson;

    public JsonResult<?> check(MeizuPayModel meizuPayModel, SdkParamCache paramCache){
        String appSecret = paramCache.channelConfig().get("channelAppSecret");
        if(Strings.isNullOrEmpty(appSecret)) {
            return new JsonResult<>(ErrorCode.ServerConfError.APP_CHANNEL_CONF_SECRET_NULL.getCode(), "appSecret conf is null");
        }
        String domain = paramCache.getChannelDomain();
        String checkpay = paramCache.channelAPI().get("api_order_query");
        domain = trimLastString(domain);
        checkpay = trimFirstString(checkpay);
        String url = domain + "/" + checkpay;

        int appId = meizuPayModel.getApp_id();
        String orderId = meizuPayModel.getCp_order_id();
        long ts = System.currentTimeMillis();
        String signType = meizuPayModel.getSign_type();

        String strToSign = "app_id=" + appId
                + "&cp_order_id=" + orderId
                + "&ts=" + ts
                + ":" + appSecret;
        String sign = MD5Util.getMD5(strToSign);

        Map<String, String> params = Maps.newHashMap();
        params.put("app_id", "" + appId);
        params.put("cp_order_id", orderId);
        params.put("ts", "" + ts);
        params.put("sign_type", "" + signType);
        params.put("sign", sign);

        String jsonResult = HttpTools.doPost(url, params);
        logger.info(jsonResult);
        Result result = gson.fromJson(jsonResult, Result.class);
        Value value = result.getValue();
        int status = value.getTradeStatus();
        switch (status) {
            case 1://待支付（订单已创建）
                return new JsonResult<Object>(ErrorCode.Success.SUCCESS.getCode(), "wait for pay");
            case 2://支付中
                return new JsonResult<Object>(ErrorCode.Success.SUCCESS.getCode(), "paying...");
            case 3://已支付
                return new JsonResult<Object>(ErrorCode.Success.SUCCESS.getCode(), "success");
            case 4://取消订单
            default:
                return new JsonResult<Object>(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode(), "failed");
        }

    }

    class Result {
        private String code;
        private String message;
        private String redirect;
        private Value value;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRedirect() {
            return redirect;
        }

        public void setRedirect(String redirect) {
            this.redirect = redirect;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }
    }

    class Value {
        private long appId;
        private String orderId;
        private String uid;
        private String productId;
        private int tradeStatus;

        public long getAppId() {
            return appId;
        }

        public void setAppId(long appId) {
            this.appId = appId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(int tradeStatus) {
            this.tradeStatus = tradeStatus;
        }
    }

    public String trimLastString(String str) {//去掉结束的"/"
        String strString = null;

        strString = str;
        if (strString.endsWith("/")) {
            strString = strString.substring(0, strString.length() - 1);
        }

        return strString;
    }

    public String trimFirstString(String str) {//去掉开头的"/"
        String strString = null;

        strString = str;
        if (strString.startsWith("/")) {
            strString = strString.substring(1, strString.length());
        }

        return strString;
    }
}
