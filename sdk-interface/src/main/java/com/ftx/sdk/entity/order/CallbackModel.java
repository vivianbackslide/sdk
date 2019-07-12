package com.ftx.sdk.entity.order;


import com.ftx.sdk.entity.orm.TSdkOrder;

import java.io.Serializable;

/**
 * @author zhenbiao.cai
 * @date 2016/6/21.
 */
public class CallbackModel implements Serializable {

    // 我们平台自己的流水号
    private String platformBillNo;
    // 渠道用户id 不回调给游戏
    private String userId;
    // 包 id
    private Integer packageId;
    // 游戏支付的时候的自定义参数
    private String appExInfo;
    // 总金额(单位分)
    private String amount;
    // 游戏的订单 Id
    private String appBillNo;
    // 渠道订单 Id
    private String channelBillNo;
    // 校验 sign
    private String sign;

    public CallbackModel(){

    }

    public CallbackModel(TSdkOrder charge){
        this.platformBillNo = String.valueOf(charge.getOrderId());
        this.userId = charge.getUserId();
        this.packageId = Integer.valueOf(charge.getPackageId());
        this.appExInfo = charge.getExInfo();
        this.amount = String.valueOf(charge.getAmount());
        this.appBillNo = charge.getGameBillNum();
        this.channelBillNo = charge.getChannelBillNum();
    }

    public String getPlatformBillNo() {
        return platformBillNo;
    }

    public void setPlatformBillNo(String platformBillNo) {
        this.platformBillNo = platformBillNo;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getAppExInfo() {
        return appExInfo;
    }

    public void setAppExInfo(String appExInfo) {
        this.appExInfo = appExInfo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAppBillNo() {
        return appBillNo;
    }

    public void setAppBillNo(String appBillNo) {
        this.appBillNo = appBillNo;
    }

    public String getChannelBillNo() {
        return channelBillNo;
    }

    public void setChannelBillNo(String channelBillNo) {
        this.channelBillNo = channelBillNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CallbackModel{" +
                "platformBillNo='" + platformBillNo + '\'' +
                ", userId='" + userId + '\'' +
                ", packageId=" + packageId +
                ", appExInfo='" + appExInfo + '\'' +
                ", amount='" + amount + '\'' +
                ", appBillNo='" + appBillNo + '\'' +
                ", channelBillNo='" + channelBillNo + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
