package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/8/26.
 */
@Component
@ChannelAnnotation(channelId = 10012, channelLabel = "yijie", channelName = "易接")
public class YijieLoginHandler extends LoginHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(YijieLoginHandler.class);

    @Autowired
    private JsonParser jsonParser;
    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        if(Strings.isNullOrEmpty(loginInfo.getExInfo())) {
            logger.error("yijie登录API异常: [exInfo is null, app_id={}, channel_id={}]", loginInfo.getAppId(), configCache.getChannelId());
            throw new ChannelLoginException("exInfo is null");
        }
        JsonObject jsonObject = jsonParser.parse(loginInfo.getExInfo()).getAsJsonObject();

        String appId_YJ = jsonObject.get("appId_YJ").getAsString();
        String channelId_YJ = jsonObject.get("channelId_YJ").getAsString();

        Map<String, String> params = Maps.newHashMap();
        params.put("sdk",  channelId_YJ);
        params.put("app", appId_YJ);
        params.put("uin", loginInfo.getUserId());
        params.put("sess", loginInfo.getToken());

        String result = HttpTools.doGet(getLoginRequestURL(configCache), params);
        logger.info("yijie dologin:[params={}]", gson.toJson(params));
        if("0".equalsIgnoreCase(result)) {
            logger.info("yijie登录API登录验证成功:[login_info={}, result={}]", loginInfo.toString(), result);
            return new User(loginInfo.getUserId(), configCache);
        }
        logger.warn("yijie登录API异常: [第三方接口返回非成功状态, channel_id={}, loginInfo={}, result={}]",
                configCache.getChannelId(), gson.toJson(loginInfo), result);
        String error = String.format("api error, result:%s, request:%s", result, params.toString());
        return null;
    }
}
