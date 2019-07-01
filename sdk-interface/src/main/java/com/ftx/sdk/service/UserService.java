package com.ftx.sdk.service;


import com.ftx.sdk.entity.orm.TGameUserRoleModel;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.StatisModel;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ServerException;
import com.ftx.sdk.exception.SignVerifyException;

/**
 * Created by zeta.cai on 2017/7/18.
 */
public interface UserService {
    User checkLoginHandle(LoginInfo loginInfo) throws ServerException, SignVerifyException;

    boolean createUserRole(TGameUserRoleModel gameRoleInfo, int appId, String channelId, String channelUserId);

    boolean roleLogin(TGameUserRoleModel gameRoleInfo, int appId, String channelId, String channelUserId);

    void subStatis(StatisModel statisInfo, String sign) throws SignVerifyException;
}
