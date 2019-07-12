package com.ftx.sdk.entity.order;


import com.ftx.sdk.entity.type.SupplementType;

import java.io.Serializable;

public class SupplementOrder implements Serializable {
    private int type; // 详见SupplementType
    private long orderId; // 如果不限制订单号，传0
    private String channelBillNum; // 不能传0。如果type为3，该参数才有用。不设置，请传空
    private int appId; // 如果不限制app，传0
    private long beginTime; // 如果不限制时间，传0。如果type为2，不能传0
    private String sign; // 验证串

    public boolean qualified() {
        if (null == SupplementType.get(type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + type +
                ", orderId=" + orderId +
                ", channelBillNum='" + channelBillNum + '\'' +
                ", appId=" + appId +
                ", beginTime=" + beginTime +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getChannelBillNum() {
        return channelBillNum;
    }

    public void setChannelBillNum(String channelBillNum) {
        this.channelBillNum = channelBillNum;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
