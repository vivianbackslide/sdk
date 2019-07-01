package com.ftx.sdk.entity.orm;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zeta.cai on 2016/8/13.
 */
@Entity
public class T_SDK_APP implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appId;
    private String appName;
    private String status;
    private String appSecret;
    private String appPayServer;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppPayServer() {
        return appPayServer;
    }

    public void setAppPayServer(String appPayServer) {
        this.appPayServer = appPayServer;
    }
}
