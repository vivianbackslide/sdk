package com.ftx.sdk.dao.channel;


import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.type.ChargeStatus;
import com.ftx.sdk.entity.type.SupplementType;

import java.util.List;

/**
 * Created by zeta.cai on 2017/7/22.
 */
public interface PlfOrderDao extends AbstractDao{
    TSdkOrder queueOrder(long orderId);

    void updateStatus(long orderId, ChargeStatus status);

    void orderUpdate(long orderId, String channelBillNo, ChargeStatus status);

    List getOrders(SupplementType type, long beginTime, int appId, long orderId);

    String getSupplementSign();

    List<TSdkOrder> queryOrderByStatus(ChargeStatus status);

    TSdkOrder createNewOrder(TSdkOrder charge);
}
