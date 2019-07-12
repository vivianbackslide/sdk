package com.ftx.sdk.entity.orm;


import com.ftx.sdk.utils.PackageUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhenbiao.cai
 * @date 2016/9/13.
 */
@Entity
@Table(name = "t_channel_charge")
public class TChannelCharge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "channel_id", nullable = false)
    private int channelId;

    @Column(name = "channel_bill_num", nullable = false)
    private String channelBillNum;

    @Column(name = "game_id")
    private int gameId;

    @Column(name = "game_bill_num", nullable = false)
    private String gameBillNum;

    @Column(name = "flp_status")
    private short flpStatus;

    @Column(name = "flp_bill_num")
    private String flpBillNum;

    @Column(name = "userid")
    private String userid;

    @Column(name = "bill_fee", nullable = false)
    private int billFee;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "exinfo")
    private String exInfo;

    @Column(name = "time")
    private Timestamp time;

    public TChannelCharge(){}

    public TChannelCharge(TSdkOrder order) {
        this.channelId = PackageUtil.getChannelId(Integer.valueOf(order.getPackageId()));
        this.channelBillNum = order.getChannelBillNum();
        this.gameId = PackageUtil.getAppId(Integer.valueOf(order.getPackageId()));
        this.gameBillNum = order.getGameBillNum();
        this.flpStatus = order.getStatus();
        this.flpBillNum = String.valueOf(order.getOrderId());
        this.userid = order.getUserId();
        this.billFee = order.getAmount();
        this.exInfo = order.getExInfo();
        this.time = order.getTime();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
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

    public String getFlpBillNum() {
        return flpBillNum;
    }

    public void setFlpBillNum(String flpBillNum) {
        this.flpBillNum = flpBillNum;
    }

    public int getBillFee() {
        return billFee;
    }

    public void setBillFee(int billFee) {
        this.billFee = billFee;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public short getFlpStatus() {
        return flpStatus;
    }

    public void setFlpStatus(short flpStatus) {
        this.flpStatus = flpStatus;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
