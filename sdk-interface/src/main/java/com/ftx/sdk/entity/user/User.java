package com.ftx.sdk.entity.user;


import com.ftx.sdk.entity.sdk.SdkParamCache;

/**
 * @author zhenbiao.cai
 * @date 2016/6/15.
 */
public class User {
    private int channelId;
    private String channelName;
    private String userId;
    /**
     * 透传参数
     */
    private String exInfo;

    public User(){}

    public User(String userId, SdkParamCache configCache){
        this.userId = userId;
        this.channelId = configCache.getChannelId();
        this.channelName = configCache.getChannelLabel();
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExInfo() {
        return exInfo;
    }

    public void setExInfo(String exInfo) {
        this.exInfo = exInfo;
    }
}
