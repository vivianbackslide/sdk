package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by lei.nie on 2016/7/11.
 */
@Component
@ChannelAnnotation(channelId = 10019, channelLabel = "tencent", channelName = "腾讯")
public class TencentLoginHandler extends LoginHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(TencentLoginHandler.class);
    @Autowired
    private Gson gson;
    @Autowired
    private JsonParser jsonParser;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        String appId = configCache.channelConfig().get("appId");
        String openKey = loginInfo.getToken();

        String exInfo = loginInfo.getExInfo();
        JsonObject jsonObject = jsonParser.parse(exInfo).getAsJsonObject();
        String openId = jsonObject.get("userId").getAsString();   //蛋疼的雷霆
        long timestamp = jsonObject.get("timestamp").getAsLong();
        boolean isWxLogin = jsonObject.get("isWxLogin").getAsBoolean();
        String keyName = isWxLogin ? "wxAppKey" : "appKey";
        String appSecret = configCache.channelConfig().get(keyName);
        String sign = MD5Util.getMD5(appSecret + timestamp);

        ManagedMap<String, String> dataMap = new ManagedMap<String, String>();
        dataMap.put("timestamp", String.valueOf(timestamp));
        dataMap.put("appid", appId);
        dataMap.put("sig", sign);
        dataMap.put("openid", openId);
        dataMap.put("openkey", openKey);

        String doamin = configCache.getChannelDomain();
        Map<String, String> channelAPI = configCache.channelAPI();
        String url = doamin + (isWxLogin ? channelAPI.get("api_login_wx") : channelAPI.get("api_login_qq"));
        String response = HttpTools.doGet(url, dataMap);
        jsonObject = jsonParser.parse(response).getAsJsonObject();
        int ret = jsonObject.get("ret").getAsInt();
        if (ret == 0) {
            return new User(loginInfo.getUserId(), configCache);
        } else {
            String message = jsonObject.get("msg").getAsString();
            logger.error("Tencent登录API异常: [第三方接口返回非成功状态, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), message);
            return null;
        }
    }
}
