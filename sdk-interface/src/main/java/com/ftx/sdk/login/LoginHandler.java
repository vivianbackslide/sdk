package com.ftx.sdk.login;


import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.exception.ServerException;

/**
 * @author zhenbiao.cai
 * @date 2016/6/15.
 */
public interface LoginHandler {
    User login(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException, ServerException;
}
