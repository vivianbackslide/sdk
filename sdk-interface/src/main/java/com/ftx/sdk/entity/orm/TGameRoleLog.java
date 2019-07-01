package com.ftx.sdk.entity.orm;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by author.chai on 2017/12/7.
 */
@Entity
@Table(name = "t_gameRole_log")
public class TGameRoleLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String plfUserId;
    private String gameRoleId;
    private Timestamp loginTime;

    public TGameRoleLog(){}

    public TGameRoleLog(String plfUserId, String roleId) {
        this.plfUserId = plfUserId;
        this.gameRoleId = roleId;
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

    public String getGameRoleId() {
        return gameRoleId;
    }

    public void setGameRoleId(String gameRoleId) {
        this.gameRoleId = gameRoleId;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }
}
