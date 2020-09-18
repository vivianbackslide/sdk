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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", loginInfo.getToken());
        params.add("appId", String.valueOf(loginInfo.getAppId()));
        params.add("userId", loginInfo.getUserId());

        String url = getLoginRequestURL(configCache);
        String response = postForm(url, params);

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

    public String postForm(String url, MultiValueMap<String, String> params) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
    }

}
