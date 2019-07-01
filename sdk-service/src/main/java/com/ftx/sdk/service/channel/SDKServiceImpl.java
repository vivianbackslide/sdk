package com.ftx.sdk.service.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.dao.channel.SdkAppDao;
import com.ftx.sdk.dao.channel.SdkChannelDao;
import com.ftx.sdk.dao.channel.SdkConfigQueueDao;
import com.ftx.sdk.dao.channel.SdkPackageDao;
import com.ftx.sdk.entity.orm.T_SDK_APP;
import com.ftx.sdk.entity.orm.T_SDK_CHANNEL;
import com.ftx.sdk.entity.orm.T_SDK_PACKAGE;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.type.PlatfromType;
import com.ftx.sdk.utils.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by zeta.cai on 2017/7/11.
 */
@Service
@org.apache.dubbo.config.annotation.Service(version = DubboConstant.VERSION)
public class SDKServiceImpl implements SDKService {

    private static final Logger logger = LoggerFactory.getLogger(SDKServiceImpl.class);

    @Autowired
    private SdkAppDao appDao;
    @Autowired
    private SdkChannelDao channelDao;
    @Autowired
    private SdkPackageDao packageDao;
    @Autowired
    private SdkConfigQueueDao configDao;

    //    @Cacheable(value = "configCache", key = "'appCache_' + #appId")
    public T_SDK_APP getAppInfo(int appId) {
        return appDao.selectById(appId, T_SDK_APP.class);
    }

    public T_SDK_CHANNEL getChannelInfo(int channelId) {
        return channelDao.selectById(channelId, T_SDK_CHANNEL.class);
    }

    public T_SDK_PACKAGE getPackageInfo(int appId, int channelId, PlatfromType type) {
        int packageId = PackageUtil.getPackage(appId, channelId, type);
        return getPackageInfo(packageId);
    }

    public T_SDK_PACKAGE getPackageInfo(int packageId) {
        return packageDao.selectById(packageId, T_SDK_PACKAGE.class);
    }


    /**
     * 首次读取时从数据库中读，并加入redis
     * 再次读取时直接从redis中读
     */
    @Cacheable(value = "configCache", key = "'packageCache_' + #packageId")//, unless = "#result == null")
    public SdkParamCache getConfig(int packageId) {
        return configDao.queueInitConfig(packageId);
    }
}
