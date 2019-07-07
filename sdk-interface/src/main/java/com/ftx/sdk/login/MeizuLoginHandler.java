package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ChannelAnnotation(channelId = 10009, channelLabel = "meizu", channelName = "魅族")
public class MeizuLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MeizuLoginHandler.class);

    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        String requestURL = getLoginRequestURL(configCache);//获取Login接口

        String appId = configCache.channelConfig().get("channelAppId");
        String sessionId = loginInfo.getToken();
        String appSecret = configCache.channelConfig().get("channelAppSecret");
        String uid = loginInfo.getUserId();
        String ts = System.currentTimeMillis() + "";
        String signType = "md5";
        String strToSign = "app_id="+appId+"&session_id=" +sessionId +"&ts=" + ts +"&uid=" + uid+":"+appSecret;
        String sign = MD5Util.getMD5(strToSign);

        Map<String, String> params = Maps.newHashMap();
        params.put("app_id", appId);
        params.put("session_id", sessionId);
        params.put("uid", uid);
        params.put("ts", "" + ts);
        params.put("sign_type", signType);
        params.put("sign", sign);

        String jsonResult = HttpTools.doPost(requestURL, params);
        logger.info(jsonResult);
        Result result = gson.fromJson(jsonResult, Result.class);

        if(result.isSuccess()) {
            logger.info("meizu渠道API登录验证成功:[loginInfo={}, result={}]", loginInfo.toString(), jsonResult);
            return new User(uid, configCache);
        } else {
            logger.warn("meizu登录API异常: [第三方接口返回非成功状态, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), jsonResult);
            return null;
        }
    }

    class Result {
        String code;
        String message;
        String redirect;
        String value;

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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        boolean isSuccess() {
            return "200".equals(code);
        }
    }
}
