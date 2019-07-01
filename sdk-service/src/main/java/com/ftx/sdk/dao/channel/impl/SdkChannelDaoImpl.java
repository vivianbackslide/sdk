package com.ftx.sdk.dao.channel.impl;

import com.ftx.sdk.dao.channel.SdkChannelDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Repository
@Transactional
public class SdkChannelDaoImpl implements SdkChannelDao {
    @Override
    public void persist(Object entity) {

    }

    @Override
    public <T> T insert(T object) {
        return null;
    }

    @Override
    public <T> void delete(T object) {

    }

    @Override
    public <T> T update(T object) {
        return null;
    }

    @Override
    public <T> T saveOrUpdate(T object) {
        return null;
    }

    @Override
    public <T> T selectById(Serializable id, Class<T> classOfT) {
        return null;
    }
}
