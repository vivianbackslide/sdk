package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.model.VivoLoginModel;
import com.ftx.sdk.utils.HttpTools;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by zeta.cai on 2016/7/18.
 */
@Component
@ChannelAnnotation(channelId = 10018, channelLabel = "vivo", channelName = "vivo")
public class VivoLoginHander extends LoginHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(VivoLoginHander.class);

    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        Map<String, String> params = Maps.newHashMap();
        params.put("authtoken", loginInfo.getToken());

        String result = HttpTools.doGet(getLoginRequestURL(configCache), params);
        if (Strings.isNullOrEmpty(result)) {
            logger.error("vivo登录API异常: [第三方接口无返回, app_id={}, channel_id={}]", loginInfo.getAppId(), configCache.getChannelId());
            return null;
        }

        VivoLoginModel loginModel = gson.fromJson(result, VivoLoginModel.class);
        if ("0".equals(loginModel.getRetcode())) {
            logger.info("vivo登录API登录验证成功:[login_info={}, result={}]", loginInfo.toString(), result);
            return new User(loginModel.getData().getOpenid(), configCache);
        } else {
            logger.warn("vivo登录API异常: [第三方接口返回非成功状态, loginInfo={}, result={}]", gson.toJson(loginInfo), result);
            return null;
        }
    }
}
