package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelAnnotation(channelId = 10066, channelLabel = "AppStore", channelName = "AppStore")
public class AppStoreLoginHandler extends LoginHandlerAdapter{
    @Autowired
    FtxLoginHandler ftxLoginHandler;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        return ftxLoginHandler.doLogin(configCache, loginInfo);
    }
}
