package com.ftx.sdk.service.channel;


import com.ftx.sdk.model.TencentPayModel;

public interface TencentPayService {
    //调用腾讯查询余额接口
    String queryBalance(TencentPayModel payModel);

    //调用腾讯扣款接口
    String deductGameCurrency(TencentPayModel payModel);

    void pay(TencentPayModel payModel);
}
