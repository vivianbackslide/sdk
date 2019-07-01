package com.ftx.sdk.dao.channel;


import com.ftx.sdk.entity.orm.TGameUserRoleModel;

/**
 * Created by zeta.cai on 2017/7/20.
 */
public interface PlfUserDao extends AbstractDao{
    boolean updateRoleLoginTime(TGameUserRoleModel gameRoleInfo);
    void createUserRole(TGameUserRoleModel gameRoleInfo);
}
