package com.ftx.sdk.model;

public class HimaPayModel {
    private String orderId;//渠道商订单号
    private String user_id;//渠道商⽤户ID
    private String system_account;//渠道商⽤户唯⼀帐号
    private String serverId;//服务器id
    private String char_id;//⽤户⻆⾊ID
    private String amount;//充值⾦额(单位：元)
    private String game_coin;//充值的游戏币
    private String offer;//赠送元宝（单件：个）
    private String cpOrderId;//游戏商订单号
    private int success;//订单状态：0成功，1失败
    private String msg;//订单失败时的错误信息
    private String sign;//验证密匙

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSystem_account() {
        return system_account;
    }

    public void setSystem_account(String system_account) {
        this.system_account = system_account;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getChar_id() {
        return char_id;
    }

    public void setChar_id(String char_id) {
        this.char_id = char_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGame_coin() {
        return game_coin;
    }

    public void setGame_coin(String game_coin) {
        this.game_coin = game_coin;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getCpOrderId() {
        return cpOrderId;
    }

    public void setCpOrderId(String cpOrderId) {
        this.cpOrderId = cpOrderId;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
