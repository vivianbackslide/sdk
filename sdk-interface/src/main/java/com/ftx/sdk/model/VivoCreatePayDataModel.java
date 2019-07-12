package com.ftx.sdk.model;


import com.ftx.sdk.utils.security.MD5Util;

/**
 * Created by lei.nie on 2017/2/14.
 */
public class VivoCreatePayDataModel {
    private int packageId;
    private String cpOrderId = "";
    private String totalPrice = "";
    private String productName = "";
    private String productDes = "";
    private String sign = "";

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getCpOrderId() {
        return cpOrderId;
    }

    public void setCpOrderId(String cpOrderId) {
        this.cpOrderId = cpOrderId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean verify(String appId) {
        String text = String.format("packageId=%s&cpOrderId=%s&totalPrice=%s&productName=%s&productDes=%s%s",
                packageId, cpOrderId, totalPrice, productName, productDes, appId);
        return sign.equals(MD5Util.getMD5(text));
    }
}
