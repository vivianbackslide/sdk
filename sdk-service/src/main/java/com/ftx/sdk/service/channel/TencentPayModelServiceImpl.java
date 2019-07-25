package com.ftx.sdk.service.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.dao.channel.TencentPayModelDao;
import com.ftx.sdk.model.TencentPayModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@org.apache.dubbo.config.annotation.Service(version = DubboConstant.VERSION)
public class TencentPayModelServiceImpl implements TencentPayModelService {

    @Autowired
    private TencentPayModelDao tencentPayModelDao;

    @Override
    public List<TencentPayModel> getAll() {
        return tencentPayModelDao.getAll();
    }

    @Override
    public void insert(TencentPayModel tencentPayModel) {
        tencentPayModelDao.insert(tencentPayModel);
    }

    @Override
    public void batchDelete(List<TencentPayModel> list) {
        tencentPayModelDao.batchDelete(list);
    }
}
