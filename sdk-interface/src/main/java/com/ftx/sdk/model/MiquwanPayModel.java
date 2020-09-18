/*
 * Copyright(c) 2017-2020, 深圳市链融科技股份有限公司
 * Beijing Xiwei Technology Co., Ltd.
 * All rights reserved.
 */
package com.ftx.sdk.model;

/**
 * @author 83454
 * @version 1.0
 * @date 2020/9/18
 */
public class MiquwanPayModel {


    private String appId;

    private String userId;

    private String channelId;

    private String orderId;

    private String orderStatus;

    private String amount;

    private String isTest;

    private String cpOrderID;

    private String callBackInfo;

    private String payTime;

    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIsTest() {
        return isTest;
    }

    public void setIsTest(String isTest) {
        this.isTest = isTest;
    }

    public String getCpOrderID() {
        return cpOrderID;
    }

    public void setCpOrderID(String cpOrderID) {
        this.cpOrderID = cpOrderID;
    }

    public String getCallBackInfo() {
        return callBackInfo;
    }

    public void setCallBackInfo(String callBackInfo) {
        this.callBackInfo = callBackInfo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
