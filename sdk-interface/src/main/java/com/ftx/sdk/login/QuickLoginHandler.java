package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by jiteng.huang on 2016/9/1.
 */
@Component
@ChannelAnnotation(channelId = 10030, channelLabel = "quick", channelName = "Quick")
public class QuickLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(QuickLoginHandler.class);

    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        Map<String, String> params = Maps.newHashMap();
        params.put("token", loginInfo.getToken());
        params.put("product_code", configCache.channelConfig().get("appId"));
        params.put("uid", loginInfo.getUserId());
        logger.info("quick dologin:[params={}]", gson.toJson(params));

        String result = HttpTools.doGet(getLoginRequestURL(configCache), params);
        if("1".equalsIgnoreCase(result)) {
            logger.info("quick登录API登录验证成功:[login_info={}, result={}]", loginInfo.toString(), result);
            return new User(loginInfo.getUserId(), configCache);
        }
        logger.error("quick登录API异常: [第三方接口返回非成功状态, channel_id={}, loginInfo={}, result={}]", configCache.getChannelId(), loginInfo.toString(), result);
        return null;
    }
}
