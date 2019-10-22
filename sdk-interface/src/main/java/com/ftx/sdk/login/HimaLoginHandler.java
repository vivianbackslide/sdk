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
@ChannelAnnotation(channelId = 20025, channelLabel = "hima", channelName = "hima")
public class HimaLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(HimaLoginHandler.class);

    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {

        Map<String, String> params = Maps.newHashMap();
        params.put("sessionid", loginInfo.getToken());

        String jsonResult = HttpTools.doPost("http://api.ximayouxi.com:8668/plat/verify", params);
        logger.info("hima login result:" + jsonResult);
        Result result = gson.fromJson(jsonResult, Result.class);

        if(result.isSuccess()) {
            logger.info("hima渠道API登录验证成功:[loginInfo={}, result={}]", loginInfo.toString(), jsonResult);
            return new User(loginInfo.getUserId(), configCache);
        } else {
            logger.warn("hima登录API异常: [第三方接口返回非成功状态, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), jsonResult);
            return null;
        }
    }


    class Result {
         int status;
         Map<String, Object> data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }

        boolean isSuccess() {
            return 1== status;
        }
    }
}
