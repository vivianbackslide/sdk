package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lei.nie on 2016/10/12.
 */
@Component
@ChannelAnnotation(channelId = 10040, channelLabel = "ftx", channelName = "范特西")
public class FtxLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(FtxLoginHandler.class);

    @Autowired
    private Gson gson;
    @Override
    public User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        logger.debug("ftx verifyToken, [loginInfo = {}]", gson.toJson(loginInfo));
        String userId = loginInfo.getUserId();
        String token = loginInfo.getToken();
        String url = getLoginRequestURL(configCache);

        logger.debug("FTXLoginRequestUrl:"+url);

        JsonObject account = new JsonObject();
        account.addProperty("userId", userId);
        account.addProperty("accessToken", token);
        String response = HttpTools.doPostByJson(url, account.toString());
        if (Strings.isNullOrEmpty(response)) {
            logger.error("ftx登录API异常: [channel no response, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), response);
            return null;
        }

        JsonObject result = new JsonParser().parse(response).getAsJsonObject();
        int code = result.get("code").getAsInt();
        String message = result.get("message").getAsString();
        if (code == 200) {
            User user = new User(userId, configCache);
            return user;
        } else {
            logger.error("ftx登录API异常: [第三方接口返回非成功状态, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), response);
            return null;
        }
    }
}
