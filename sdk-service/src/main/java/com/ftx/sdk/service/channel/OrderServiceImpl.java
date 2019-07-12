package com.ftx.sdk.service.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.dao.channel.PlfOrderDao;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.type.ChargeStatus;
import com.ftx.sdk.entity.type.SupplementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by zeta.cai on 2017/7/22.
 */
@Service
@org.apache.dubbo.config.annotation.Service(version = DubboConstant.VERSION)
public class OrderServiceImpl implements OrderService {

    private static final Long EPOCH = 1483200000000L;
    private static final DecimalFormat decimalFormat = new DecimalFormat("000000");

    @Autowired
    private PlfOrderDao orderDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @CachePut(value = "order", key = " 'order_' + #charge.orderId")
    public TSdkOrder createNewOrder(TSdkOrder charge) {
        StringBuilder id = new StringBuilder().append(System.currentTimeMillis() - EPOCH).append(decimalFormat.format(new Random().nextInt(1000000)));
        charge.setOrderId(Long.valueOf(id.toString()));
        return orderDao.createNewOrder(charge);
    }

    @Cacheable(value = "order", key = " 'order_' + #orderId", unless = "#result == null")
    public TSdkOrder queueOrder(long orderId) {
        return orderDao.queueOrder(orderId);
    }

    @CacheEvict(value = "order", key = " 'order_' + #orderId")
    public void orderComplete(long orderId, String channelBillNo) {
        orderDao.orderUpdate(orderId, channelBillNo, ChargeStatus.PaySuccessNotifySuccess);
    }

    @CacheEvict(value = "order", key = " 'order_' + #orderId")
    public void orderUpdateOnFirstCallback(long orderId, String channelBillNo, ChargeStatus status) {
        orderDao.orderUpdate(orderId, channelBillNo, status);
    }

    @CacheEvict(value = "order", key = " 'order_' + #orderId")
    public void updateStatus(long orderId, ChargeStatus status) {
        orderDao.updateStatus(orderId, status);
    }

    @CacheEvict(value = "order", key = " 'order_' + #charge.orderId")
    public void illegalAmountHandler(TSdkOrder charge) {
        String sql = "update t_sdk_order set status = ? where orderId = ?";
        Object[] object = {(short) ChargeStatus.IllegalAmount.getType(), charge.getOrderId()};
        jdbcTemplate.update(sql, object);
    }

    @CacheEvict(value = "order", key = " 'order_' + #charge.orderId")
    public void update(TSdkOrder charge) {
        orderDao.update(charge);
    }

    public void VerifyFailed(TSdkOrder charge) {
        String sql = "update t_sdk_order set status = ? where orderId = ?";
        //Object[] object = {(short) ChargeStatus.VerifyFailed.getType(), charge.getOrderId()};
        //jdbcTemplate.update(sql, object);
    }

    public List getOrders(SupplementType type, long beginTime, int appId, long orderId) {
        return orderDao.getOrders(type, beginTime, appId, orderId);
    }

    public String getSupplementSign() {
        return orderDao.getSupplementSign();
    }

    @Override
    public List<TSdkOrder> queryOrderByStatus(ChargeStatus status) {
        return orderDao.queryOrderByStatus(status);
    }
}
