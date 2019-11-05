package com.ftx.sdk.model;

public class JisuhudongPayModel {
    private String amount;//订单金额，单位为分，例例 100 (1元)
    private String cp_extend;//⼚商透传参数
    private String cp_order_no;//厂商订单号
    private String order_no;//SDK订单号
    private String pay_time;//⽀付时间戳，单位秒
    private String uid;//SDK⽤户唯一ID
    private String sign;//验证密匙

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCp_extend() {
        return cp_extend;
    }

    public void setCp_extend(String cp_extend) {
        this.cp_extend = cp_extend;
    }

    public String getCp_order_no() {
        return cp_order_no;
    }

    public void setCp_order_no(String cp_order_no) {
        this.cp_order_no = cp_order_no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
