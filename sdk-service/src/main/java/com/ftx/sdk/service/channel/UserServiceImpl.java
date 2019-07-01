package com.ftx.sdk.service.channel;

import com.ftx.sdk.annotation.GameRoleLog;
import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.dao.channel.PlfUserDao;
import com.ftx.sdk.entity.orm.TGameUserRoleModel;
import com.ftx.sdk.entity.orm.TLoginLog;
import com.ftx.sdk.entity.orm.TRealTimeData;
import com.ftx.sdk.entity.orm.TUserMap;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.type.PlatfromType;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.StatisModel;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.exception.ServerException;
import com.ftx.sdk.exception.SignVerifyException;
import com.ftx.sdk.login.LoginHandlerManager;
import com.ftx.sdk.service.UserService;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.PackageUtil;
import com.ftx.sdk.utils.VerifyUitl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by zeta.cai on 2017/7/18.
 */
@Service
@org.apache.dubbo.config.annotation.Service(version = DubboConstant.VERSION)
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private SDKService sdkService;
    @Autowired
    private LoginHandlerManager loginManager;
    @Autowired
    private PlfUserDao userDao;
    @Autowired
    private ExecutorService EXECUTOR;
    @Autowired
    private Gson gson;

    @Override
    public User checkLoginHandle(LoginInfo loginInfo) throws ServerException, SignVerifyException {

        SdkParamCache paramConfig = sdkService.getConfig(loginInfo.getPackageId());
        if (paramConfig == null)
            throw new ChannelLoginException("无效的packageId");

        if (!VerifyUitl.verifyLoginInfo(loginInfo, paramConfig.getAppSecret()))   //校验sign
            throw new SignVerifyException("verify user sign error");

        User user = loginManager.getLoginHandler(paramConfig.getChannelId()).login(paramConfig, loginInfo);    //获取对应渠道，执行渠道的登录方法

        if (user == null)
            return null;

        //对易接做特殊处理，将渠道号替换成真实渠道
        if (user.getChannelId() == 10012) {
            Map<String, String> exInfo = gson.fromJson(loginInfo.getExInfo(), new TypeToken<Map<String, String>>() {
            }.getType());
            user.setChannelId(Integer.parseInt(exInfo.get("channelId")));
        }

        EXECUTOR.execute(new AsyncRecordUser(user, loginInfo));  //数据异步入库

        return user;
    }


    @Override
    public void subStatis(StatisModel statisInfo, String sign) throws SignVerifyException {

        SdkParamCache cache = sdkService.getConfig(PackageUtil.getPackage(statisInfo.getAppId(), statisInfo.getChannelId(), PlatfromType.Android));

        if (!VerifyUitl.verify(MapsUtils.object2StringMap(statisInfo), sign, cache.getAppSecret())) {
            throw new SignVerifyException("sign error");
        }

        userDao.insert(new TRealTimeData(statisInfo));
    }

    @Override
    public boolean createUserRole(TGameUserRoleModel gameRoleInfo, int appId, String channelId, String channelUserId) {
        gameRoleInfo.setPlfUserId(channelUserId + "_" + appId + "_" + channelId);
        gameRoleInfo.checkRoleId();
        if (gameRoleInfo.getLastLoginTime() == null)
            gameRoleInfo.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        if (!gameRoleInfo.qulified()) {
            return false;
        }
        //userDao.insert(gameRoleInfo);
        userDao.createUserRole(gameRoleInfo);
        return true;
    }

    @Override
    @GameRoleLog(loggedApp = {309})
    public boolean roleLogin(TGameUserRoleModel gameRoleInfo, int appId, String channelId, String channelUserId) {
        gameRoleInfo.setPlfUserId(channelUserId + "_" + appId + "_" + channelId);
        return userDao.updateRoleLoginTime(gameRoleInfo);
    }

    /**
     * 内部类，实现Runnable，用于用户数据的入库
     * 渠道游戏用户映射表：t_cp_channel_user_map 用于记录账户信息，包括创建时间、最后登陆时间和支付状态等
     * 用户登录日志表： t_account_log 用于记录每次用户登录，该表主要用于计算留存
     */
    private class AsyncRecordUser implements Runnable {

        private User user;
        private LoginInfo loginInfo;

        public AsyncRecordUser(User user, LoginInfo loginInfo) {
            this.user = user;
            this.loginInfo = loginInfo;
        }

        @Override
        public void run() {
            TLoginLog log = new TLoginLog(loginInfo.getAppId(), user.getChannelId(), user.getUserId());
            TUserMap userInfo = new TUserMap(user.getUserId(), loginInfo.getAppId(), user.getChannelId());

            userDao.persist(log);
            userDao.saveOrUpdate(userInfo);     //数据库中不存在id则新增，存在则更新lastLoginTime（ 其余字段全部定义了@Column(updatable = false) ）
        }
    }
}
