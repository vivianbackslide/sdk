package com.ftx.sdk.dao.channel.impl;

import com.ftx.sdk.dao.channel.PlfUserDao;
import com.ftx.sdk.entity.orm.TGameUserRoleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Created by zeta.cai on 2017/7/20.
 */
@Repository("PlfUserDaos")
@Transactional
public class PlfUserDaoImpl implements PlfUserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean updateRoleLoginTime(TGameUserRoleModel gameRoleInfo) {
        String sql = "UPDATE `t_cp_userRole` SET `lastLoginTime`= CURRENT_TIME WHERE (`plfUserId`= ?) AND (`roleId`= ?)";
        Object[] object = {gameRoleInfo.getPlfUserId(), gameRoleInfo.getRoleId()};
        return 0 != jdbcTemplate.update(sql, object);
    }

    @Override
    public void createUserRole(TGameUserRoleModel gameRoleInfo) {
        String sql = "insert into t_cp_userRole (plfUserId, roleId, roleName, roleLevel, zoneId, zoneName, balance, vipLevel, partyName, createTime, roleLevelChangeTime, lastLoginTime, lastLogoutTime) SELECT ?,?,?,?,?,?,?,?,?,?,?,?,? FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM t_cp_userRole WHERE plfUserId = ? AND roleId = ?)";
        Object[] object = {gameRoleInfo.getPlfUserId(), gameRoleInfo.getRoleId(), gameRoleInfo.getRoleName(), gameRoleInfo.getRoleLevel(), gameRoleInfo.getZoneId(), gameRoleInfo.getZoneName(), gameRoleInfo.getBalance(), gameRoleInfo.getVipLevel(), gameRoleInfo.getPartyName(), gameRoleInfo.getCreateTime(), gameRoleInfo.getRoleLevelChangeTime(), gameRoleInfo.getLastLoginTime(), gameRoleInfo.getLastLogoutTime(),gameRoleInfo.getPlfUserId(),gameRoleInfo.getRoleId()};
        jdbcTemplate.update(sql, object);
    }

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
