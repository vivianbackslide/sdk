package com.ftx.sdk.entity.orm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by zeta.cai on 2016/8/16.
 */
@Entity
public class T_SDK_CHANNEL implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int channelId;
    private String channelName;
    private String channelLabel;
    private String callBackUrl;
    private String channelDomain;
    private String channelAPI;
    private String channelConfigMap;

    @Transient
    private Map<String, String> channelAPIMap;

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

    public String getChannelDomain() {
        return channelDomain;
    }

    public void setChannelDomain(String channelDomain) {
        this.channelDomain = channelDomain;
    }

    public Map<String, String> getChannelAPI() {
        if (channelAPIMap == null)
            channelAPIMap =  new Gson().fromJson(channelAPI, new TypeToken<Map<String, String>>(){}.getType());

        return channelAPIMap;
    }

    public void setChannelAPI(String channelAPI) {
        this.channelAPI = channelAPI;
    }

    public void setChannelAPI(Map<String, String> channelAPI) {
        this.channelAPI = new Gson().toJson(channelAPI);
    }

    public Map<String, String> getChannelConfigMap() {
        return new Gson().fromJson(channelConfigMap, new TypeToken<Map<String, String>>(){}.getType());
    }

    public void setChannelConfigMap(String channelConfigMap) {
        this.channelConfigMap = channelConfigMap;
    }

    public void setChannelConfigMap(Map<String, String> channelConfigMap) {
        this.channelConfigMap = new Gson().toJson(channelConfigMap);
    }
}
