package com.ftx.sdk.dao.channel;


import com.ftx.sdk.entity.sdk.SdkParamCache;

/**
 * Created by zeta.cai on 2017/7/12.
 */
public interface SdkConfigQueueDao {
    SdkParamCache queueInitConfig(int packageId);
}
