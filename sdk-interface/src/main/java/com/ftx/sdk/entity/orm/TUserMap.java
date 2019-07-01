package com.ftx.sdk.entity.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by zeta.cai on 2017/2/13.
 */
@Entity
@Table(name = "t_cp_channel_user_map")
public class TUserMap {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String plfUserId;

    @Column(updatable = false, columnDefinition = "varChar(25) default ''")
    private String channelUserId = "";

    @Column(updatable = false)
    private int appId;

    @Column(updatable = false)
    private int channelId;

    @Column(updatable = false)
    private Timestamp createTime;

    private Timestamp lastLoginTime;

    @Column(updatable = false)
    private boolean isPay;

    public TUserMap() {}

    public TUserMap(String channelUserId, int appId, int channelId) {
        this.channelUserId = channelUserId;
        this.appId = appId;
        this.channelId = channelId;

        this.plfUserId = channelUserId + "_" + appId + "_" + channelId;
        this.isPay = false;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.lastLoginTime = this.createTime;
    }

    public String getPlfUserId() {
        return plfUserId;
    }

    public void setPlfUserId(String plfUserId) {
        this.plfUserId = plfUserId;
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }
}
