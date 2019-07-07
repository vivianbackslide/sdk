package com.ftx.sdk.model;

import org.hibernate.validator.constraints.NotEmpty;

public class UCPayModelData {
    // 充值订单号, 此订单号由 UC 游戏 SDK 生成， 游戏 客户端在进行充值时从 SDK 获得。
    @NotEmpty(message = "orderId is null")
    private String orderId;

    // 游戏编号, 由 UC 分配
    @NotEmpty(message = "gameId is null")
    private String gameId;

    // 账号标识
    @NotEmpty(message = "accountId is null")
    private String accountId;

    // 账号的创建者
    @NotEmpty(message = "creator is null")
    private String creator;

    // 支付通道代码
    @NotEmpty(message = "payWay is null")
    private String payWay;

    // 支付金额, 单位：元。游戏服务端必须根据 UC    回调的金额值进行校验并下发相应价    值的虚拟货币。
    @NotEmpty(message = "amount is null")
    private String amount;

    // 游戏合作商自    定义参数
    @NotEmpty(message = "callbackInfo is null, json need:orderId,packageId,gameZoneId,currency,itemId")
    private String callbackInfo;

    // 订单状态
    @NotEmpty(message = "orderStatus is null")
    private String orderStatus;

    // 订单失败原因   详细描述
    @NotEmpty(message = "failedDesc is null")
    private String failedDesc;

    // Cp 订单,仅当客户端调用支付方法传入了    transactionNumCP 参数时， 才会将    原内容通过 cpOrderId 参数透传回    游戏服务端。该参数有传递时，才需    加入签名，如无，则不需要加入签名            (注意：长度不超过 30）
    // 因为长度太短，所以游戏订单ID放到透传参数里面
    private String cpOrderId;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCallbackInfo() {
        return callbackInfo;
    }

    public void setCallbackInfo(String callbackInfo) {
        this.callbackInfo = callbackInfo;
    }

    public String getFailedDesc() {
        return failedDesc;
    }

    public void setFailedDesc(String failedDesc) {
        this.failedDesc = failedDesc;
    }

    public String getCpOrderId() {
        return cpOrderId;
    }

    public void setCpOrderId(String cpOrderId) {
        this.cpOrderId = cpOrderId;
    }
}
