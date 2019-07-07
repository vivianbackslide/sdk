package com.ftx.sdk.model;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zhenbiao.cai
 * @date 2016/8/26.
 */
public class YijiePayModel {

    @NotBlank(message = "app is null")
    private String app;

    //用这个字段来透传订单 id
    @NotBlank(message = "ct is null")
    private String cbi;

    @NotBlank(message = "ct is null")
    private String ct;

    //金额（分）
    @NotBlank(message = "fee is null")
    private String fee;

    @NotBlank(message = "pt is null")
    private String pt;

    @NotBlank(message = "sdk is null")
    private String sdk;

    //订单在渠道平台上的流水号
    @NotBlank(message = "ssid is null")
    private String ssid;

    @NotBlank(message = "st is null")
    private String st;

    //订单在易接服务器上的订单号
    @NotBlank(message = "tcd is null")
    private String tcd;

    //付费用户在渠道平台上的唯一标记
    @NotBlank(message = "uid is null")
    private String uid;

    @NotBlank(message = "ver is null")
    private String ver;

    @NotBlank(message = "sign is null")
    private String sign;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getCbi() {
        return cbi;
    }

    public void setCbi(String cbi) {
        this.cbi = cbi;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getTcd() {
        return tcd;
    }

    public void setTcd(String tcd) {
        this.tcd = tcd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
