package com.ftx.sdk.login.xiaomi;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.login.LoginHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lei.jiang on 2017/9/14.
 */
@Component
@ChannelAnnotation(channelId = 10091, channelLabel = "xiaomijs", channelName = "小米(金山)")
public class XiaomiJsLoginHandler extends LoginHandlerAdapter {
    @Autowired
    private XiaomiLoginHandler xiaomiLoginHandler;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        return xiaomiLoginHandler.doLogin(configCache,loginInfo);
    }
}
