package com.ftx.sdk.entity.orm;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhenbiao.cai
 * @date 2016/11/24.
 */
@Entity
@Table(name = "t_login_log")
public class TLoginLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "plf_appid", nullable = false)
    private int plfAppId;

    @Column(name = "plf_channelid", nullable = false)
    private int plfChannelId;

    @Column(name = "plf_channelUserid", nullable = false)
    private String plfChannelUserId;

    @Column(name = "time")
    private Timestamp lastLoginTime;

    public TLoginLog(){}

    public TLoginLog(int plfAppId, int plfChannelId, String plfChannelUserId){
        this.plfAppId = plfAppId;
        this.plfChannelId = plfChannelId;
        this.plfChannelUserId = plfChannelUserId;
    }

    public String toString() {
        return String.format("id=%d, plfAppId=%d, plfChannelId=%d, plfChannelUserId=%s", id, plfAppId, plfChannelId, plfChannelUserId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlfAppId() {
        return plfAppId;
    }

    public void setPlfAppId(int plfAppId) {
        this.plfAppId = plfAppId;
    }

    public int getPlfChannelId() {
        return plfChannelId;
    }

    public void setPlfChannelId(int plfChannelId) {
        this.plfChannelId = plfChannelId;
    }

    public String getPlfChannelUserId() {
        return plfChannelUserId;
    }

    public void setPlfChannelUserId(String plfChannelUserId) {
        this.plfChannelUserId = plfChannelUserId;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
