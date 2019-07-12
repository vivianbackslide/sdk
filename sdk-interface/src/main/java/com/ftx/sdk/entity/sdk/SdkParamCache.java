package com.ftx.sdk.entity.sdk;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/6/15.
 * SDK AppPackage配置参数
 */
public class SdkParamCache implements Serializable {

    //包id
    private Integer packageId;
    // 游戏在我们平台的编号
    //==========   app参数   ============
    private Integer appId;
    // 游戏和我们平台校验的key
    private transient String appSecret;
    // 游戏回调地址
    private transient String appPayServer;
    //==========   channel参数   ============
    //渠道Id
    private transient int channelId;
    //渠道标识码
    private String channelLabel;
    //支付回调地址
    private String callBackUrl;
    //渠道服务器域名
    private transient String channelDomain;
    //渠道接口
    private transient String channelAPI;
    //==========   package参数   ============
    //这个包的渠道配置
    private transient String status;
    //这个包的渠道配置
    private transient String channelConfig;
    //==========   逻辑参数   ============
    @SerializedName("channelConfig")
    private Map<String, String> channelConfigMap;
    private transient Map<String, String> channelAPIMap;


    public SdkParamCache(){}

    public String verson(){
        return null;
    }

    public Map<String, String> channelConfig() {
        if (channelConfigMap == null){
            channelConfigMap = new Gson().fromJson(channelConfig, new TypeToken<Map<String, String>>(){}.getType());
        }

        return channelConfigMap;
    }

    public String channelConfig(String key) {
        if (channelConfig().containsKey(key))
            return channelConfig().get(key);
        return null;
    }

    public Map<String, String> channelAPI() {
        if (channelAPIMap == null){
            channelAPIMap = new Gson().fromJson(channelAPI, new TypeToken<Map<String, String>>(){}.getType());
        }

        return channelAPIMap;
    }

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

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelLabel() {
        return channelLabel;
    }

    public void setChannelLabel(String channelLabel) {
        this.channelLabel = channelLabel;
    }

    public String getChannelDomain() {
        return channelDomain;
    }

    public void setChannelDomain(String channelDomain) {
        this.channelDomain = channelDomain;
    }

    public String getChannelAPI() {
        return channelAPI;
    }

    public void setChannelAPI(String channelAPI) {
        this.channelAPI = channelAPI;
    }

    public String getChannelConfig() {
        return channelConfig;
    }

    public void setChannelConfig(String channelConfig) {
        this.channelConfig = channelConfig;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getAppPayServer() {
        return appPayServer;
    }

    public void setAppPayServer(String appPayServer) {
        this.appPayServer = appPayServer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean release(){
        if (Strings.isNullOrEmpty(status) || status.equalsIgnoreCase("0"))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SdkParamCache{" +
                "packageId=" + packageId +
                ", appId=" + appId +
                ", appSecret='" + appSecret + '\'' +
                ", appPayServer='" + appPayServer + '\'' +
                ", channelId=" + channelId +
                ", channelLabel='" + channelLabel + '\'' +
                ", callBackUrl='" + callBackUrl + '\'' +
                ", channelDomain='" + channelDomain + '\'' +
                ", channelAPI='" + channelAPI + '\'' +
                ", status='" + status + '\'' +
                ", channelConfig='" + channelConfig + '\'' +
                ", channelConfigMap=" + channelConfigMap +
                ", channelAPIMap=" + channelAPIMap +
                '}';
    }
}
