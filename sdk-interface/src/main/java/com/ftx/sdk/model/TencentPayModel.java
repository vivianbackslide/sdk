package com.ftx.sdk.model;


import javax.validation.constraints.NotNull;

/**
 * Created by lei.nie on 2016/7/14.
 */
public class TencentPayModel {
    @NotNull(message = "openId is null")
    private String openId;

    @NotNull(message = "openKey is null")
    private String openKey;

    @NotNull(message = "pf is null")
    private String pf;

    @NotNull(message = "pfKey is null")
    private String pfKey;

    @NotNull(message = "zoneId is Null")
    private Integer zoneId;

    @NotNull(message = "billno is null")
    private String billno;

    @NotNull(message = "isQQ is Null")
    private Integer isQQ;

    private int amt;

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public int getIsQQ() {
        return isQQ;
    }

    public void setIsQQ(int isQQ) {
        this.isQQ = isQQ;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getPfKey() {

        return pfKey;
    }

    public void setPfKey(String pfKey) {
        this.pfKey = pfKey;
    }

    public String getPf() {

        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getOpenKey() {

        return openKey;
    }

    public void setOpenKey(String openKey) {
        this.openKey = openKey;
    }

    public String getOpenId() {

        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "TencentPayModel{" +
                "openId='" + openId + '\'' +
                ", openKey='" + openKey + '\'' +
                ", pf='" + pf + '\'' +
                ", pfKey='" + pfKey + '\'' +
                ", zoneId=" + zoneId +
                ", billno='" + billno + '\'' +
                ", isQQ=" + isQQ +
                ", amt=" + amt +
                '}';
    }
}
