package com.ftx.sdk.model;

import java.io.Serializable;

/**
 * Facebook活动礼包发放model
 */
public class FacebookGiftModel implements Serializable {

    /**
     * 领取礼包的用户uid
     */
    private String uid;
    /**
     * 游戏礼包活动唯一值，表示用户要领取的哪个活动礼包
     */
    private String giftNo;
    /**
     * 游戏跳转活动页面是传递的游戏serverId，原样返回
     */
    private String serverInfo;
    /**
     * 游戏跳转活动页面是传递的游戏roleId，原样返回
     */
    private String roleInfo;
    /**
     * 签名值，游戏应根据签名约定，本地计算后与此值进行比对
     */
    private String sign;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGiftNo() {
        return giftNo;
    }

    public void setGiftNo(String giftNo) {
        this.giftNo = giftNo;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public String getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(String roleInfo) {
        this.roleInfo = roleInfo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "FacebookGiftModel{" +
                "uid='" + uid + '\'' +
                ", giftNo='" + giftNo + '\'' +
                ", serverInfo='" + serverInfo + '\'' +
                ", roleInfo='" + roleInfo + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
