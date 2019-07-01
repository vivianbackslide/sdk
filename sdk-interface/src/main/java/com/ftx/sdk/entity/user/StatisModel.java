package com.ftx.sdk.entity.user;

import javax.validation.constraints.NotNull;

/**
 * Created by zeta.cai on 2017/6/28.
 */
public class StatisModel {
    @NotNull(message = "appId is Null")
    private Integer appId;
    @NotNull(message = "channelId is Null")
    private Integer channelId;
    @NotNull(message = "online is Null")
    private Integer online;

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

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
