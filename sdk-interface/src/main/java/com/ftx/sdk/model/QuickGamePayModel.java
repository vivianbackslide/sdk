package com.ftx.sdk.model;

/**
 * Created by runmin.han on 2019/6/4.
 */
public class QuickGamePayModel {
    private String uid;//购买道具的用户uid
    private String username;//购买道具的用户username
    private String cpOrderNo;//游戏下单时传递的游戏订单号，原样返回
    private String orderNo;//SDK唯一订单号
    private String payTime;//用户支付时间，如2017-02-06 14:22:32
    private String payAmount;//用户支付金额
    private String payCurrency;//用户支持的币种，如RMB，USD等
    private String usdAmount;//用户支付的游戏道具以美元计价的金额
    private String payStatus;//支付状态，为0表示成功，为1时游戏不做处理
    private String actRate;//充值折扣，取值范围0.1~1，默认为1表示不折扣；如值为0.2表示多发20%的元宝
    private String extrasParams;//游戏下单时传递的扩展参数，将原样返回。
    private String sign;//签名值，游戏应根据签名约定，本地计算后与此值进行比对

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCpOrderNo() {
        return cpOrderNo;
    }

    public void setCpOrderNo(String cpOrderNo) {
        this.cpOrderNo = cpOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayCurrency() {
        return payCurrency;
    }

    public void setPayCurrency(String payCurrency) {
        this.payCurrency = payCurrency;
    }

    public String getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(String usdAmount) {
        this.usdAmount = usdAmount;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getActRate() {
        return actRate;
    }

    public void setActRate(String actRate) {
        this.actRate = actRate;
    }

    public String getExtrasParams() {
        return extrasParams;
    }

    public void setExtrasParams(String extrasParams) {
        this.extrasParams = extrasParams;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
