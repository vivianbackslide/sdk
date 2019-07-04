package com.ftx.sdk.controller.channel;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.login.LoginHandlerAdapter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by runmin.han on 2018/9/17.
 */

@Component
@ChannelAnnotation(channelId = 20015, channelLabel = "Maoer", channelName = "猫耳")
public class MaoerLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MaoerLoginHandler.class);

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        logger.info("猫耳UID：" + loginInfo.getToken());
        String uid = loginInfo.getToken();
        if (Strings.isNullOrEmpty(uid)) {
            logger.error("猫耳登录失败: [第三方回调uid为空, uid={}, channel_id={}]", loginInfo.getToken(), configCache.getChannelId());
            return null;
        } else {
            logger.info("猫耳登录API登录验证成功:[第三方回调uid, uid={}, channel_id={}]", loginInfo.getToken(), configCache.getChannelId());
            return new User(uid, configCache);
        }
    }
}
