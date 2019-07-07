package com.ftx.sdk.model;

import com.google.common.base.Strings;
import org.hibernate.validator.constraints.NotBlank;

import java.util.*;

/**
 * Created by lei.nie on 2016/7/11.
 */
public class QihooPayModel {
    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_return() {

        return sign_return;
    }

    public void setSign_return(String sign_return) {
        this.sign_return = sign_return;
    }

    public String getApp_order_id() {

        return app_order_id;
    }

    public void setApp_order_id(String app_order_id) {
        this.app_order_id = app_order_id;
    }

    public String getSign_type() {

        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getGateway_flag() {

        return gateway_flag;
    }

    public void setGateway_flag(String gateway_flag) {
        this.gateway_flag = gateway_flag;
    }

    public long getOrder_id() {

        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getUser_id() {

        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getApp_ext2() {

        return app_ext2;
    }

    public void setApp_ext2(String app_ext2) {
        this.app_ext2 = app_ext2;
    }

    public String getApp_ext1() {

        return app_ext1;
    }

    public void setApp_ext1(String app_ext1) {
        this.app_ext1 = app_ext1;
    }

    public String getApp_uid() {

        return app_uid;
    }

    public void setApp_uid(String app_uid) {
        this.app_uid = app_uid;
    }

    public int getAmount() {

        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProduct_id() {

        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @NotBlank(message = "app_key is null")
    private String app_key;

    @NotBlank(message = "product_id is null")
    private String product_id;

    private int amount;

    @NotBlank(message = "app_uid is null")
    private String app_uid;

    private String app_ext1;
    private String app_ext2;
    private long user_id;
    private long order_id;

    @NotBlank(message = "gateway_flag is null")
    private String gateway_flag;

    @NotBlank(message = "sign_type is null")
    private String sign_type;

    private String app_order_id;

    @NotBlank(message = "sign_return is null")
    private String sign_return;

    @NotBlank(message = "sign is null")
    private String sign;

    private Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("app_key", app_key);
        map.put("product_id", product_id);
        map.put("amount", String.valueOf(amount));
        map.put("app_uid", app_uid);
        map.put("user_id", String.valueOf(user_id));
        map.put("order_id", String.valueOf(order_id));
        map.put("gateway_flag", gateway_flag);
        map.put("sign_type", sign_type);

        if(!Strings.isNullOrEmpty(app_ext1)){
            map.put("app_ext1", app_ext1);
        }
        if(!Strings.isNullOrEmpty(app_ext2)){
            map.put("app_ext2", app_ext2);
        }
        if(!Strings.isNullOrEmpty(app_order_id)){
            map.put("app_order_id", app_order_id);
        }
        return map;
    }

    public String toSortString(){
        Map<String, String> map = toMap();
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < keys.size(); i++){
            String key = keys.get(i);
            String value = map.get(key);
            if(!Strings.isNullOrEmpty(value)){
                if(i != 0){
                    sb.append("#");
                }
                sb.append(value);
            }
        }
        return sb.toString();
    }
}
