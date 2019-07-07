package com.ftx.sdk.login.xiaomi;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.login.LoginHandlerAdapter;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.security.HmacSHA1Encryption;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/6/25.
 */
@Component
@ChannelAnnotation(channelId = 10013, channelLabel = "xiaomi", channelName = "小米")
public class XiaomiLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(XiaomiLoginHandler.class);

    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        if(Strings.isNullOrEmpty(loginInfo.getUserId())) {
            throw new ChannelLoginException("uid null");
        }
        // 计算签名
        try {

        String src = "appId=" + configCache.channelConfig().get("channelAppId") + "&session=" + loginInfo.getToken() + "&uid=" + loginInfo.getUserId();
        String sign = HmacSHA1Encryption.HmacSHA1Encrypt(src, configCache.channelConfig().get("channelAppSecret"));
        Map<String, String> requestParam = Maps.newHashMap();
        requestParam.put("appId", configCache.channelConfig().get("channelAppId"));
        requestParam.put("session", loginInfo.getToken());
        requestParam.put("uid", loginInfo.getUserId());
        requestParam.put("signature", sign);
        String requestURL = getLoginRequestURL(configCache);
        String jsonResult = HttpTools.doGet(requestURL, requestParam);
        if (Strings.isNullOrEmpty(jsonResult)) {
            logger.error("小米登录API: [第三方接口无返回, app_id={}, channel_id={}]", loginInfo.getAppId(), configCache.getChannelId());
            return null;
        }
        XiaoMiResult xiaoMiResult = gson.fromJson(jsonResult, XiaoMiResult.class);
        if (xiaoMiResult.isSuccess()) {
            return new User(loginInfo.getUserId(), configCache);
        } else {
            logger.error("小米登录API异常: [第三方接口返回非成功状态, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), jsonResult);
            return null;
        }
        } catch (Exception e) {
            throw new ChannelLoginException("小米登录失败" + e.getMessage());
        }
    }
}
