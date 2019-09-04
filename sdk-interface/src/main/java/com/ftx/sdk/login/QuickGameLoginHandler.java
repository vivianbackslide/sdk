package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by runmin.han on 2019/6/3.
 */
@Component
@ChannelAnnotation(channelId = 20023, channelLabel = "QuickGame", channelName = "QuickGame")
public class QuickGameLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(QuickGameLoginHandler.class);
    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        String token = loginInfo.getToken();//用户token
        String uid = loginInfo.getUserId();//用户ID

        String url = getLoginRequestURL(configCache);//渠道登录验证接口地址

        logger.debug("QuickGameLogin:token={}, uid={}", token, uid);

        Map<String,String> params = new HashMap<>();
        params.put("token", token);
        params.put("uid", uid);

        String result = HttpTools.doGet(url, params);//登录验证获取结果

        Result response = gson.fromJson(result, Result.class);

        if (Strings.isNullOrEmpty(result)){
            logger.error("QuickGame登录API异常: [第三方接口返回结果失败, app_id={}, channel_id={}]", loginInfo.getAppId(), configCache.getChannelId());
            return null;
        }

        if (response.isStatus()==true){
            logger.info("QuickGame登录API登录验证成功:[第三方接口返回成功状态, status={}, data={}]", response.isStatus(), response.getMessage());
            return new User(uid, configCache);
        }else{
            logger.error("QuickGame登录API异常:[第三方接口返回非成功状态, status={}, message={}]", response.isStatus(), response.getMessage());
            return null;
        }

    }

    class Result {
        boolean status;//接口验证状态，若通过验证为true，否则为false
        String message;//Status为false时，message有值，为错误提示语

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
