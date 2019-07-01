package com.ftx.sdk.entity.orm;


import com.ftx.sdk.utils.VerifyUitl;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zeta.cai on 2017/6/9.
 */
@Entity
@Table(name = "t_cp_userRole")
public class TGameUserRoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String plfUserId;

    private String roleId;

    private String roleName;

    private int roleLevel;

    private int zoneId;

    private String zoneName;

    private int balance;

    private int vipLevel;

    private String partyName;

    @Column(name = "createTime")
    private Timestamp roleCreateTime;

    private Timestamp roleLevelChangeTime;

    private Timestamp lastLoginTime;

    private Timestamp lastLogoutTime;

    public void checkRoleId(){
        if (roleId.equalsIgnoreCase("1e+15")) {
            roleId = "fake" + getRoleName();
        }
    }

    public boolean qulified(){
        if (!VerifyUitl.ifTimestampIsMillsecond(roleCreateTime)) {
            return false;
        }
        if (!VerifyUitl.ifTimestampIsMillsecond(roleLevelChangeTime)) {
            return false;
        }
        if (!VerifyUitl.ifTimestampIsMillsecond(lastLoginTime)) {
            return false;
        }
        if (!VerifyUitl.ifTimestampIsMillsecond(lastLogoutTime)) {
            return false;
        }
        return true;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlfUserId() {
        return plfUserId;
    }

    public void setPlfUserId(String plfUserId) {
        this.plfUserId = plfUserId;
    }

    public String getRoleId() {
        checkRoleId();
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Timestamp getCreateTime() {
        return roleCreateTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.roleCreateTime = createTime;
    }

    public Timestamp getRoleLevelChangeTime() {
        return roleLevelChangeTime;
    }

    public void setRoleLevelChangeTime(Timestamp roleLevelChangeTime) {
        this.roleLevelChangeTime = roleLevelChangeTime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Timestamp getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Timestamp lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }
}
