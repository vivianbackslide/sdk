package com.ftx.sdk.model;

/**
 * Created by runmin.han on 2018/9/19.
 */
public class MaoerPayModel {
    private String amount;//成功充值金额(元)
    private String callback_info;//返回给游戏商的私有字段，客户端extradata字段传递(非必选)
    private String order_id;//订单ID，合作方订单系统唯一号
    private String role_id;//游戏角色ID
    private String server_id;//游戏服ID
    private int status;//订单状态，1为成功
    private int timestamp;//时间戳
    private int type;//充值类型
    private String user_id;//SDK用户ID
    private String extend;//扩展参数(此参数不参与签名)，预留
    private String sign;//验证密匙

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCallback_info() {
        return callback_info;
    }

    public void setCallback_info(String callback_info) {
        this.callback_info = callback_info;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
