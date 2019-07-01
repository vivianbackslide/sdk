package com.ftx.sdk.dao.channel.impl;

import com.ftx.sdk.dao.channel.SdkConfigQueueDao;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SdkConfigQueueDaoImpl implements SdkConfigQueueDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public SdkParamCache queueInitConfig(int packageId) {
        String sql = "SELECT packageId,T_SDK_PACKAGE.appId,appSecret,appPayServer,T_SDK_PACKAGE.channelId,channelLabel,callBackUrl,channelDomain,channelAPI,T_SDK_PACKAGE.status,channelConfig from T_SDK_PACKAGE INNER JOIN T_SDK_CHANNEL on packageId = ? and T_SDK_PACKAGE.channelId = T_SDK_CHANNEL.channelId INNER JOIN T_SDK_APP ON T_SDK_PACKAGE.appId = T_SDK_APP.appId";
        Object[] object = {packageId};
        List<SdkParamCache> sdkParamCaches = jdbcTemplate.queryForList(sql, object, SdkParamCache.class);

        return sdkParamCaches.isEmpty() ? null : sdkParamCaches.get(0);
    }
}
