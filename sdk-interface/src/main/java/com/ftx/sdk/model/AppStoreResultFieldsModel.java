package com.ftx.sdk.model;

/**
 * Created by zeta.cai on 2017/2/27.
 */
public class AppStoreResultFieldsModel {
    private String quantity;    //数量
    private String product_id;  //商品id
    private String transaction_id;  //商品标识id  订单
    private String original_transaction_id;  //恢复先前交易的transaction原始标识id
    private String purchase_date;   //购买日期
    private String original_purchase_date;   //恢复先前交易的transaction原始日期
    private String expires_date;    //商品到期日期
    private String cancellation_date;   //对于由Apple客户支持取消的交易，取消的时间和日期
    private String app_item_id; //app唯一标识
    private String version_external_identifier; //app版本
    private String web_order_line_item_id;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOriginal_transaction_id() {
        return original_transaction_id;
    }

    public void setOriginal_transaction_id(String original_transaction_id) {
        this.original_transaction_id = original_transaction_id;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getOriginal_purchase_date() {
        return original_purchase_date;
    }

    public void setOriginal_purchase_date(String original_purchase_date) {
        this.original_purchase_date = original_purchase_date;
    }

    public String getExpires_date() {
        return expires_date;
    }

    public void setExpires_date(String expires_date) {
        this.expires_date = expires_date;
    }

    public String getCancellation_date() {
        return cancellation_date;
    }

    public void setCancellation_date(String cancellation_date) {
        this.cancellation_date = cancellation_date;
    }

    public String getApp_item_id() {
        return app_item_id;
    }

    public void setApp_item_id(String app_item_id) {
        this.app_item_id = app_item_id;
    }

    public String getVersion_external_identifier() {
        return version_external_identifier;
    }

    public void setVersion_external_identifier(String version_external_identifier) {
        this.version_external_identifier = version_external_identifier;
    }

    public String getWeb_order_line_item_id() {
        return web_order_line_item_id;
    }

    public void setWeb_order_line_item_id(String web_order_line_item_id) {
        this.web_order_line_item_id = web_order_line_item_id;
    }
}
