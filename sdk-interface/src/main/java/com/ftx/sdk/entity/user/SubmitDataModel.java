package com.ftx.sdk.entity.user;


import com.ftx.sdk.entity.orm.TGameUserRoleModel;

/**
 * Created by zeta.cai on 2017/6/9.
 */
public class SubmitDataModel {
    private int appId;
    private String channelId;
    private String channelUserId;
    private int type;
    private TGameUserRoleModel user;

    public boolean qulified(){
        return user.qulified();
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TGameUserRoleModel getUser() {
        return user;
    }

    public void setUser(TGameUserRoleModel user) {
        this.user = user;
    }
}
