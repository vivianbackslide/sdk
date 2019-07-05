package com.ftx.sdk.vo;

import java.io.Serializable;
import java.util.Map;

public class ConfigVO implements Serializable {

    private Integer packageId;
    private Integer appId;
    private String channelLabel;
    private String callBackUrl;
    private Map<String, String> channelConfig;

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getChannelLabel() {
        return channelLabel;
    }

    public void setChannelLabel(String channelLabel) {
        this.channelLabel = channelLabel;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public Map<String, String> getChannelConfig() {
        return channelConfig;
    }

    public void setChannelConfig(Map<String, String> channelConfig) {
        this.channelConfig = channelConfig;
    }

    @Override
    public String toString() {
        return "ConfigVO{" +
                "packageId=" + packageId +
                ", appId=" + appId +
                ", channelLabel='" + channelLabel + '\'' +
                ", callBackUrl='" + callBackUrl + '\'' +
                ", channelConfig=" + channelConfig +
                '}';
    }
}
