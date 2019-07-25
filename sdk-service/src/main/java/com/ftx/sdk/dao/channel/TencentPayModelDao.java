package com.ftx.sdk.dao.channel;

import com.ftx.sdk.model.TencentPayModel;

import java.util.List;

public interface TencentPayModelDao {
    List<TencentPayModel> getAll();

    void insert(TencentPayModel tencentPayModel);

    void batchDelete(List<TencentPayModel> list);
}
