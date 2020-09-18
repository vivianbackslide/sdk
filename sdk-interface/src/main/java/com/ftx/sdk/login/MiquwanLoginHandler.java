/*
 * Copyright(c) 2017-2020, 深圳市链融科技股份有限公司
 * Beijing Xiwei Technology Co., Ltd.
 * All rights reserved.
 */
package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 83454
 * @version 1.0
 * @date 2020/9/18
 */
@Component
@ChannelAnnotation(channelId = 20028, channelLabel = "miquwan", channelName = "米趣玩")
public class MiquwanLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MiquwanLoginHandler.class);

    @Autowired
    private JsonParser jsonParser;
    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        logger.debug("米趣玩登录验证: [loginInfo={}]", gson.toJson(loginInfo));

        JsonObject jsonObject = new JsonObject();

        Map<String, Object> map = new HashMap<>(4);
        map.put("token", loginInfo.getToken());
        map.put("appId", loginInfo.getAppId());
        map.put("userId", loginInfo.getUserId());

        String url = getLoginRequestURL(configCache);
        String response = HttpTools.doPostByConnection(url, gson.toJson(map));

        logger.info("米趣玩登录响应报文：{}", response);

        if ("1".equals(response)) {
            logger.debug("米趣玩登录API登录验证成功:[login_info={}, result={}]", loginInfo.toString(), response);
            User user = new User();
            user.setChannelId(configCache.getChannelId());
            user.setChannelName(configCache.getChannelLabel());
            user.setUserId(loginInfo.getUserId());
            return user;
        } else {
            logger.error("米趣玩登录API异常: [第三方接口返回非成功状态, logininfo={}, request={}, result={}]", gson.toJson(loginInfo), jsonObject.toString(), response);
            return null;
        }
    }
}
