package com.ftx.sdk.entity.orm;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by zeta.cai on 2016/8/17.
 */
@Entity
public class T_SDK_PACKAGE implements Serializable {
    @Id
    private Integer packageId;
    private String status;
    private Integer channelId;
    private Integer appId;
    private String packageName;
    private String channelConfig;

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Map<String, String> getChannelConfigMap() {
        return new Gson().fromJson(channelConfig, new TypeToken<Map<String, String>>(){}.getType());
    }

    public String getChannelConfig() {
        return channelConfig;
    }

    public void setChannelConfig(Map<String, String> channelConfig) {
        this.channelConfig = new Gson().toJson(channelConfig);
    }

    public void setChannelConfig(String channelConfig) {
        this.channelConfig = channelConfig;
    }
}
