package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ChannelAnnotation(channelId = 20026, channelLabel = "jisuhudong", channelName = "极速互动")
public class JisuhudongLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(JisuhudongLoginHandler.class);

    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {

        Map<String, String> params = Maps.newHashMap();
        params.put("token", loginInfo.getToken());
        params.put("time", String.valueOf(System.currentTimeMillis()));

        String signStr = MapsUtils.createLinkString(params, true, false);
        signStr = signStr + "&secret=" + configCache.channelConfig().get("channelAppKey");
        logger.info("极速互动原始签名sign = {}", signStr);
        String mySign = MD5Util.getMD5(signStr);
        params.put("sign", mySign);

        logger.info("jshd login params = {}", gson.toJson(params));

        String jsonResult = HttpTools.doGet("https://api.jisuhudong.com/cp/v1/members/token", params);
        logger.info("jisuhudong login result:" + jsonResult);
        Result result = gson.fromJson(jsonResult, Result.class);

        if (result.isSuccess()) {
            logger.info("jisuhudong渠道API登录验证成功:[loginInfo={}, result={}]", loginInfo.toString(), jsonResult);
            return new User(result.getData().get("uid"), configCache);
        } else {
            logger.warn("jisuhudonga登录API异常: [第三方接口返回非成功状态, app_id={}, channel_id={}, result={}]", loginInfo.getAppId(), configCache.getChannelId(), jsonResult);
            return null;
        }
    }


    class Result {
        int code;
        String msg;
        Map<String, String> data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Map<String, String> getData() {
            return data;
        }

        public void setData(Map<String, String> data) {
            this.data = data;
        }

        boolean isSuccess() {
            return 200 == code;
        }
    }
}
