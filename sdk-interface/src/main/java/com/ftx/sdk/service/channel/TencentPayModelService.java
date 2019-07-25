package com.ftx.sdk.service.channel;

import com.ftx.sdk.model.TencentPayModel;

import java.util.List;

public interface TencentPayModelService {

    List<TencentPayModel> getAll();

    void insert(TencentPayModel tencentPayModel);

    void batchDelete(List<TencentPayModel> list);
}
