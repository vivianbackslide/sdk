package com.ftx.sdk.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by jiteng.huang on 2016/7/12.
 */
public class MeizuPayModel{
    //APPID (不能为空)
    @NotNull(message = "app_id is null")
    private int app_id;

    //订单ID (不能为空)
    @NotEmpty(message = "cp_order_id is null")
    private String cp_order_id;

    //用户ID(不能为空)
    @NotNull(message = "uid is null")
    private long uid;

    //商品ID,默认值：”0”
    private String product_id;

    //订单标题,格式为：”购买N枚金币”
    private String product_subject;

    //商品说明，默认值：””
    private String product_body;

    //商品单位，默认值：””
    private String product_unit;

    //购买数量，默认值：”1”
    private int buy_amount;

    //商品单价，默认值：总金额
    private String product_per_price;

    //总金额
    private String total_price;

    //创建时间
    private long create_time;

    //支付方式，默认值：”0”（即定额支付）
    private int pay_type;

    //自定义信息，默认值：""
    private String user_info;

    //参数签名 (不能为空)
    @NotEmpty(message = "sign is null")
    private String sign;

    //签名算法，默认值："md5 (不能为空)"
    @NotEmpty(message = "sign_type is null")
    private String sign_type;

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getCp_order_id() {
        return cp_order_id;
    }

    public void setCp_order_id(String cp_order_id) {
        this.cp_order_id = cp_order_id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_subject() {
        return product_subject;
    }

    public void setProduct_subject(String product_subject) {
        this.product_subject = product_subject;
    }

    public String getProduct_body() {
        return product_body;
    }

    public void setProduct_body(String product_body) {
        this.product_body = product_body;
    }

    public String getProduct_unit() {
        return product_unit;
    }

    public void setProduct_unit(String product_unit) {
        this.product_unit = product_unit;
    }

    public int getBuy_amount() {
        return buy_amount;
    }

    public void setBuy_amount(int buy_amount) {
        this.buy_amount = buy_amount;
    }

    public String getProduct_per_price() {
        return product_per_price;
    }

    public void setProduct_per_price(String product_per_price) {
        this.product_per_price = product_per_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }
}
