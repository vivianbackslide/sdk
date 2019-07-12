package com.ftx.sdk.controller.channel;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.login.LoginHandlerAdapter;
import com.ftx.sdk.model.OppoOauth;
import com.ftx.sdk.utils.HttpTools;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

/**
 * @author zhenbiao.cai
 * @date 2016/7/1.
 */
@Component
@ChannelAnnotation(channelId = 10010, channelLabel = "oppo", channelName = "oppo")
public class OppoLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(OppoLoginHandler.class);

    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        String appKey = configCache.channelConfig().get("channelAppKey");
        String appSecret = configCache.channelConfig().get("channelAppSecret");
        String oauthTimestamp = "" + System.currentTimeMillis();
        String oauthNonce = "" + new Random().nextInt(100000000);
        String oauthToken = loginInfo.getToken();
        String ssoid = loginInfo.getUserId();

        String baseStr = OppoOauth.generateBaseString(appKey, oauthToken, oauthTimestamp, oauthNonce);
        String signStr = OppoOauth.generateSign(baseStr, appSecret);
        Map<String, String> headers = Maps.newHashMap();
        headers.put("param", baseStr);
        headers.put("oauthSignature", signStr);

        Map<String, String> params = Maps.newHashMap();
        params.put("fileId", ssoid);
        params.put("token", oauthToken);
        String jsonResult = HttpTools.doGetWithHeaders(getLoginRequestURL(configCache), params, headers);
        if (Strings.isNullOrEmpty(jsonResult)) {
            logger.error("oppo登录API: [第三方接口无返回, app_id={}, channel_id={}]", loginInfo.getAppId(), configCache.getChannelId());
            return null;
        }
        OppoResult oppoResult = gson.fromJson(jsonResult, OppoResult.class);
        if(null != oppoResult && oppoResult.isSuccess(ssoid)) {
            logger.info("oppo渠道API登录验证成功:[loginInfo={}, result={}]", loginInfo.toString(), jsonResult);
            return new User(ssoid, configCache);
        } else {
            logger.error("oppo渠道API异常: [第三方接口返回非成功状态, package_id={}, result={}]", loginInfo.getPackageId(), jsonResult);
            return null;
        }
    }

    class OppoResult {
        String resultCode;
        String resultMsg;
        String ssoid;

        boolean isSuccess(String uid) {
            if(null != ssoid && ssoid.equalsIgnoreCase(uid) && "200".equalsIgnoreCase(resultCode)) {
                return true;
            }
            return false;
        }
    }
}
