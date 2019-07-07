package com.ftx.sdk.model.xiaomi;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author zhenbiao.cai
 * @date 2016/6/27.
 */
public class XiaoMiPayModel {

    //必须	游戏ID
    @NotEmpty(message = "appId is null")
    private String appId;

    //必须	开发商订单ID
    @NotEmpty(message = "cpOrderId is null")
    private String cpOrderId;

    //可选	开发商透传信息
    private String cpUserInfo;

    //必须	用户ID
    @NotEmpty(message = "uid is null")
    private String uid;

    //必须	游戏平台订单ID
    @NotEmpty(message = "orderId is null")
    private String orderId;

    //必须	订单状态，TRADE_SUCCESS 代表成功
    @NotEmpty(message = "orderStatus is null")
    private String orderStatus;

    //必须	支付金额,单位为分,即0.01 米币。
    @NotEmpty(message = "payFee is null")
    private String payFee;

    //必须	商品代码
    @NotEmpty(message = "productCode is null")
    private String productCode;

    //必须	商品名称
    @NotEmpty(message = "productName is null")
    private String productName;

    //必须	商品数量
    @NotEmpty(message = "productCount is null")
    private String productCount;

    //必须	支付时间,格式 yyyy-MM-dd HH:mm:ss
    @NotEmpty(message = "payTime is null")
    private String payTime;

    //可选	订单类型：10：普通订单11：直充直消订单
    private String orderConsumeType;

    //必选	使用游戏券金额 （如果订单使用游戏券则有,long型），如果有则参与签名
//    @NotEmpty(message = "partnerGiftConsume is null")
    private String partnerGiftConsume;

    //必须	签名,签名方法见后面说明
    @NotEmpty(message = "signature is null")
    private String signature;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCpOrderId() {
        return cpOrderId;
    }

    public void setCpOrderId(String cpOrderId) {
        this.cpOrderId = cpOrderId;
    }

    public String getCpUserInfo() {
        return cpUserInfo;
    }

    public void setCpUserInfo(String cpUserInfo) {
        this.cpUserInfo = cpUserInfo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPayFee() {
        return payFee;
    }

    public void setPayFee(String payFee) {
        this.payFee = payFee;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrderConsumeType() {
        return orderConsumeType;
    }

    public void setOrderConsumeType(String orderConsumeType) {
        this.orderConsumeType = orderConsumeType;
    }

    public String getPartnerGiftConsume() {
        return partnerGiftConsume;
    }

    public void setPartnerGiftConsume(String partnerGiftConsume) {
        this.partnerGiftConsume = partnerGiftConsume;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
