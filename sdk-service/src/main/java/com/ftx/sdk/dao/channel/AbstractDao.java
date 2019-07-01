package com.ftx.sdk.dao.channel;

import java.io.Serializable;

/**
 * Created by zeta.cai on 2017/6/9.
 */
public interface AbstractDao {
    public void persist(Object entity);

    public <T> T insert(T object);

    public <T> void delete(T object);

    public <T> T update(T object);

    public <T> T saveOrUpdate(T object);

    public <T> T selectById(Serializable id, Class<T> classOfT);
}
