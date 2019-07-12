package com.ftx.sdk.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by zeta.cai on 2016/7/18.
 */
public class OppoPayModel {
    @NotEmpty(message = "notifyId is null")
    private String notifyId;
    @NotEmpty(message = "partnerOrder is null")
    private String partnerOrder;
    @NotEmpty(message = "productName is null")
    private String productName;
    @NotEmpty(message = "productDesc is null")
    private String productDesc;
    //商品价格(以分为单位)
    private int price;
    //商品数量（一般为1）
    private int count;
    private String attach;
    @NotEmpty(message = "sign is null")
    private String sign;

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getPartnerOrder() {
        return partnerOrder;
    }

    public void setPartnerOrder(String partnerOrder) {
        this.partnerOrder = partnerOrder;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
