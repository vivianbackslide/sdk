package com.ftx.sdk.entity.orm;

import com.ftx.sdk.utils.VerifyUitl;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author zhenbiao.cai
 * @date 2016/9/13.
 */
@Entity
@Table(name = "t_sdk_order")
public class TSdkOrder {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(generator = "orderIdGenerator")
    @GenericGenerator(name = "orderIdGenerator", strategy = "com.ftx.sdk.server.dao.hibernate.OrderIdGenerator")
    private Long orderId;

    @Column(name = "packageId", nullable = false)
    private String packageId;

    @Column(name = "channel_bill_num")
    private String channelBillNum;

    @Column(name = "game_bill_num", nullable = false)
    private String gameBillNum;

    @Column(name = "status", nullable = false)
    private short status;

    @Column(name = "channelUserId", nullable = false)
    private String userId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "exInfo")
    private String exInfo;

    @Column(name = "notifyUrl")
    private String notifyUrl = "";

    @Column(name = "time", nullable = false)
    private Timestamp time;

    private Timestamp completedTime;

    public boolean qualified(){
        if (!VerifyUitl.verifyPackageId(Integer.parseInt(packageId))){
            return false;
        }
        return true;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getChannelBillNum() {
        return channelBillNum;
    }

    public void setChannelBillNum(String channelBillNum) {
        this.channelBillNum = channelBillNum;
    }

    public String getGameBillNum() {
        return gameBillNum;
    }

    public void setGameBillNum(String gameBillNum) {
        this.gameBillNum = gameBillNum;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getExInfo() {
        return exInfo;
    }

    public void setExInfo(String exInfo) {
        this.exInfo = exInfo;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Timestamp getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Timestamp completedTime) {
        this.completedTime = completedTime;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
