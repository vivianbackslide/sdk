package com.ftx.sdk.service.channel;


import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;

/**
 * Created by zeta.cai on 2017/8/8.
 */
public interface CallbackService {
    void orderHandler(TSdkOrder charge, SdkParamCache paramCache);

    void addToPool(TSdkOrder charge, SdkParamCache paramCache);
}
