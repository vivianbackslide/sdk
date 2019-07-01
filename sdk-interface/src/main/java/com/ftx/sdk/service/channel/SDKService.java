package com.ftx.sdk.service.channel;


import com.ftx.sdk.entity.orm.T_SDK_APP;
import com.ftx.sdk.entity.sdk.SdkParamCache;

/**
 * Created by zeta.cai on 2017/7/11.
 */
public interface SDKService {
    T_SDK_APP getAppInfo(int appId);
//
//    T_SDK_CHANNEL getChannelInfo(int channelId);
//
//    T_SDK_PACKAGE getPackageInfo(int appId, int channelId, PlantfromType type);
//
//    T_SDK_PACKAGE getPackageInfo(int packageId);

    SdkParamCache getConfig(int packageId);
}
