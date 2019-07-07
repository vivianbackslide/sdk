package com.ftx.sdk.model;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by lei.nie on 2016/7/9.
 */
public class BaiduPayModel {
    private int AppID;

    @NotBlank(message = "OrderSerial is null")
    private String OrderSerial;

    @NotBlank(message = "CooperatorOrderSerial is null")
    private String CooperatorOrderSerial;

    @NotBlank(message = "Sign is null")
    private String Sign;

    @NotBlank(message = "Content is null")
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSign() {

        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getCooperatorOrderSerial() {

        return CooperatorOrderSerial;
    }

    public void setCooperatorOrderSerial(String cooperatorOrderSerial) {
        CooperatorOrderSerial = cooperatorOrderSerial;
    }

    public String getOrderSerial() {

        return OrderSerial;
    }

    public void setOrderSerial(String orderSerial) {
        OrderSerial = orderSerial;
    }

    public int getAppID() {

        return AppID;
    }

    public void setAppID(int appID) {
        AppID = appID;
    }
}
