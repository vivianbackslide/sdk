package com.ftx.sdk.dao.channel.impl;

import com.ftx.sdk.dao.channel.PlfOrderDao;
import com.ftx.sdk.entity.orm.TChannelCharge;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.type.ChargeStatus;
import com.ftx.sdk.entity.type.SupplementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zeta.cai on 2017/7/22.
 */
@Repository
@Transactional
public class PlfOrderDaoImpl implements PlfOrderDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public TSdkOrder queueOrder(long orderId) {
        String sql = "select orderId, packageId, channel_bill_num channelBillNum, game_bill_num gameBillNum, `status`, channelUserId userId, amount, exInfo, notifyUrl, time from t_sdk_order where orderId = ?";
        Object[] object = {orderId};
        return jdbcTemplate.queryForObject(sql, object, BeanPropertyRowMapper.newInstance(TSdkOrder.class));
    }

    @Override
    public void updateStatus(long orderId, ChargeStatus status) {
        String sql = "UPDATE t_sdk_order SET status = ? WHERE orderId = ?";
        jdbcTemplate.update(sql, status, orderId);
    }

    @Override
    public void orderUpdate(long orderId, String channelBillNo, ChargeStatus status) {
        String sql = "UPDATE t_sdk_order SET channel_bill_num = ? ,status = ? WHERE orderId = ?";
        Object[] objects0 = {channelBillNo, status.getType(), orderId};
        jdbcTemplate.update(sql, objects0);

        // TODO: 添加数据到t_channel_charge, 目的是匹配到旧版本的运营数据， 等全面改用新版时删除对应代码
        if (status == ChargeStatus.PaySuccessNotifySuccess) {
            TSdkOrder order = queueOrder(orderId);
            String insertSql = "insert into t_channel_charge (channel_id, channel_bill_num, game_id, game_bill_num, flp_status, flp_bill_num, userid, bill_fee, exinfo, time) values (?,?,?,?,?,?,?,?,?,?)";
            TChannelCharge tChannelCharge = new TChannelCharge(order);
            Object[] objects = {tChannelCharge.getChannelId(), tChannelCharge.getChannelBillNum(), tChannelCharge.getGameId(), tChannelCharge.getGameBillNum(), tChannelCharge.getFlpStatus(), tChannelCharge.getFlpBillNum(), tChannelCharge.getUserid(), tChannelCharge.getBillFee(), tChannelCharge.getExInfo(), tChannelCharge.getTime()};
            jdbcTemplate.update(insertSql, objects);
        }

    }

    @Override
    public List getOrders(SupplementType type, long beginTime, int appId, long orderId) {
        String sql = "select packageId, channel_bill_num, game_bill_num, status, channelUserId, amount, exInfo, notifyUrl, time from t_sdk_order where 1=1 ";
        switch (type) {
            case Failure_Resend:
                sql += " and (status = " + (short) 3 + " or status = " + (short) 4 + ")";
                break;
            case Success_Resend:
                sql += " and status = " + (short) 5 + "";
                break;
            case ChannelBug:
                sql += " and status = " + (short) 0 + "";
                break;
            default:
                break;
        }
        if (beginTime != 0) {
            sql += " and time > " + new Date(beginTime);
        }
        if (appId != 0) {
            sql += " and packageId like '" + appId + "%'";
        }
        if (orderId != 0) {
            sql += " and orderId = " + orderId;
        }
        sql += " limit 100";

        return jdbcTemplate.queryForList(sql, new BeanPropertyRowMapper<>(TSdkOrder.class));


//        String sql = "SELECT * from t_sdk_order WHERE status = 3";
//
//        return getSession().createSQLQuery(sql)
//                .addEntity(TSdkOrder. class )
//                .list();
    }

    @Override
    public String getSupplementSign() {
        String sql = "SELECT password from T_SDKWEB_USER WHERE userName = 'supplement'";

        return jdbcTemplate.queryForObject(sql, String.class);

    }

    @Override
    public List<TSdkOrder> queryOrderByStatus(ChargeStatus status) {
        String sql = "select orderId, packageId, channel_bill_num channelBillNum, game_bill_num gameBillNum, `status`, channelUserId userId, amount, exInfo, notifyUrl, time from t_sdk_order where status = ?";
        Object[] object = {status};
        return jdbcTemplate.query(sql, object, new BeanPropertyRowMapper<>(TSdkOrder.class));
    }

    @Override
    public TSdkOrder createNewOrder(TSdkOrder charge) {
        String sql = "INSERT INTO t_sdk_order(orderId, packageId, channel_bill_num, game_bill_num, channelUserId, amount, exInfo, `status`, time, completedTime, notifyUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] objects = {charge.getOrderId(), charge.getPackageId(), charge.getChannelBillNum(), charge.getGameBillNum(), charge.getUserId(), charge.getAmount(), charge.getExInfo(), charge.getStatus(), charge.getTime(), charge.getCompletedTime(), charge.getNotifyUrl()};
        int update = jdbcTemplate.update(sql, objects);
        if (update > 0) {
            return charge;
        }
        return null;
    }

    @Override
    public void persist(Object entity) {

    }

    @Override
    public <T> T insert(T object) {
        return null;
    }

    @Override
    public <T> void delete(T object) {

    }

    @Override
    public <T> T update(T object) {
        return null;
    }

    @Override
    public <T> T saveOrUpdate(T object) {
        return null;
    }

    @Override
    public <T> T selectById(Serializable id, Class<T> classOfT) {
        return null;
    }
}
