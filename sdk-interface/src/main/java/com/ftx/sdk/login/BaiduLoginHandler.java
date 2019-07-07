package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.baidu.Base64;
import com.ftx.sdk.utils.baidu.Sdk;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/7/1.
 */
@Component
@ChannelAnnotation(channelId = 10006, channelLabel = "bd", channelName = "百度")
public class BaiduLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(BaiduLoginHandler.class);

    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) {

        String appId =configCache.channelConfig().get("appId");
        String accessToken = loginInfo.getToken();
        String secretKey = configCache.channelConfig().get("channelAppSecret");
        String sign = MD5Util.getMD5(appId + accessToken + secretKey);
        Map<String, String> params = Maps.newHashMap();
        params.put("AppID", appId);
        params.put("AccessToken", accessToken);
        params.put("Sign", sign);

        String jsonResult = HttpTools.doPost(getLoginRequestURL(configCache), params);
        if (Strings.isNullOrEmpty(jsonResult)) {
            logger.error("baidu登录API异常: [第三方接口无返回, app_id={}, channel_id={}]", loginInfo.getAppId(), configCache.getChannelId());
            return null;
        }

        Result result = gson.fromJson(jsonResult, Result.class);
        try {
            if (result.isSuccess(secretKey)) {
                logger.info("baidu登录API登录验证成功:[login_info={}, result={}]", loginInfo.toString(), jsonResult);
                String contentJson = URLDecoder.decode(Base64.decode(result.Content), "UTF-8");
                Content content = gson.fromJson(contentJson, Content.class);
                User user = new User();
                user.setUserId(content.UID.toString());
                user.setChannelId(configCache.getChannelId());
                user.setChannelName(configCache.getChannelLabel());
                return user;
            } else {
                logger.error("baidu登录API异常: [第三方接口返回非成功状态, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), jsonResult);
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("baidu登录API异常: [第三方接口解析异常, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), jsonResult);
            return null;
        }
    }

    class Result {
        int AppID;
        int ResultCode;
        String ResultMsg;
        String Sign;
        String Content;

        boolean isSuccess(String appSecret) throws UnsupportedEncodingException {
            if (ResultCode == 1){
                String content = URLDecoder.decode(Content,"utf-8");
                String contentbyte = Base64.decode(content);
                String contentEncode = Base64.encode(contentbyte);
                Sdk sdk = new Sdk();
                String sig = sdk.md5(String.valueOf(AppID) + ResultCode + contentEncode + appSecret);
                return sig.equalsIgnoreCase(Sign);
            }
            return false;
        }
    }

    class Content {
        Long UID;
    }
}
