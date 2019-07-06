package com.ftx.sdk.service.channel;


import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.type.ChargeStatus;
import com.ftx.sdk.entity.type.SupplementType;

import java.util.List;

/**
 * Created by zeta.cai on 2017/7/22.
 */
public interface OrderService {
    TSdkOrder createNewOrder(TSdkOrder order);
    TSdkOrder queueOrder(long orderId);
    void orderComplete(long orderId, String channelBillNo);
    void orderUpdateOnFirstCallback(long orderId, String channelBillNo, ChargeStatus status);
    void updateStatus(long orderId, ChargeStatus status);

    void illegalAmountHandler(TSdkOrder charge);
    void VerifyFailed(TSdkOrder charge);

    void update(TSdkOrder charge);

    List getOrders(SupplementType type, long beginTime, int appId, long orderId);
    String getSupplementSign();
    List<TSdkOrder> queryOrderByStatus(ChargeStatus status);

    void orderUpdate(long orderId, String channelBillNo, ChargeStatus status);
}
