package com.ftx.sdk.entity.orm;


import com.ftx.sdk.entity.user.StatisModel;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zeta.cai on 2017/6/29.
 */
@Entity
@Table(name = "t_real_time_data")
public class TRealTimeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int appId;
    private int channelId;
    private Timestamp time;
    private int onlineNum;

    public TRealTimeData(StatisModel statistics){
        this.appId = statistics.getAppId();
        this.channelId = statistics.getChannelId();
        this.onlineNum = statistics.getOnline();
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }
}
