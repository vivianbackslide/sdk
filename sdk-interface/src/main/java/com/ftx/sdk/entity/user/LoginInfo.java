package com.ftx.sdk.entity.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.validation.constraints.NotNull;

/**
 * @author zhenbiao.cai
 * @date 2016/6/15.
 */
public class LoginInfo {
    /**
     * 在SDK平台分配的应用ID
     */
    @NotNull(message = "appId is Null")
    private Integer appId;
    /**
     * 包ID
     */
    @NotNull(message = "packageId is Null")
    private Integer packageId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 验证token
     */
    @NotNull(message = "token is Null")
    private String token;
    /**
     * 透传参数
     */
    private String exInfo;

    @NotNull(message = "sign is Null")
    private String sign;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExInfo() {
        return exInfo;
    }

    public void setExInfo(String exInfo) {
        this.exInfo = exInfo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static LoginInfo instanceOfJson(String json){

        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);

        LoginInfo loginInfo = new LoginInfo();
        if (jsonObject.get("appId") != null)
            loginInfo.setAppId(jsonObject.get("appId").getAsInt());
        if (jsonObject.get("packageId") != null)
            loginInfo.setPackageId(jsonObject.get("packageId").getAsInt());
        if (jsonObject.get("token") != null)
            loginInfo.setToken(jsonObject.get("token").getAsString());
        if (jsonObject.get("userId") != null)
            loginInfo.setUserId(jsonObject.get("userId").getAsString());
        if (jsonObject.get("sign") != null)
            loginInfo.setSign(jsonObject.get("sign").getAsString());
        if (jsonObject.get("exInfo") != null) {
            String exInfo = null;
            try {
                exInfo = jsonObject.get("exInfo").getAsJsonObject().toString();
            }catch (IllegalStateException e){
                exInfo = jsonObject.get("exInfo").getAsString();
            }
            loginInfo.setExInfo(exInfo);
        }

        return loginInfo;
    }
}
