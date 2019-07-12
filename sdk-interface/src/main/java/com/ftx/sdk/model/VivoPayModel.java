package com.ftx.sdk.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by zeta.cai on 2016/7/18.
 */
public class VivoPayModel {
    @NotEmpty(message = "respCode is null")
    private String respCode;
    @NotEmpty(message = "respMsg is null")
    private String respMsg;
    private String signMethod;
    @NotEmpty(message = "signature is null")
    private String signature;
    @NotEmpty(message = "tradeType is null")
    private String tradeType;
    @NotEmpty(message = "tradeStatus is null")
    private String tradeStatus;
    @NotEmpty(message = "cpId is null")
    private String cpId;
    @NotEmpty(message = "appId is null")
    private String appId;
    @NotEmpty(message = "uid is null")
    private String uid;
    @NotEmpty(message = "cpOrderNumber is null")
    private String cpOrderNumber;
    @NotEmpty(message = "orderNumber is null")
    private String orderNumber;
    //交易金额 单位：分s
    @NotEmpty(message = "orderAmount is null")
    private String orderAmount;
    private String extInfo;
    @NotEmpty(message = "payTime is null")
    private String payTime;
    private String channelInfo;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCpOrderNumber() {
        return cpOrderNumber;
    }

    public void setCpOrderNumber(String cpOrderNumber) {
        this.cpOrderNumber = cpOrderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(String channelInfo) {
        this.channelInfo = channelInfo;
    }
}
