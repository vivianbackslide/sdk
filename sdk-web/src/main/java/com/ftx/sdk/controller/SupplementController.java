package com.ftx.sdk.controller;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.ConfigBean;
import com.ftx.sdk.entity.order.SupplementOrder;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.entity.type.SupplementType;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.Constants;
import com.ftx.sdk.utils.VerifyUitl;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by author.chai on 2018/9/20.
 */
@RestController
public class SupplementController {
    private Logger logger = LoggerFactory.getLogger(SupplementController.class);

    @Autowired
    private Gson gson;

    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;

    private static final String LocalIp = "172.20.101.50";
    //    private static final String LocalIp = "192.168.12.41"; // 本地测试使用自己的ip
    private static final String ServerLocalHost =
            LocalIp + ":" + ConfigBean.getIntValue(ConfigBean.SERVER_PORT);

    @RequestMapping("/supplement")
    // 一次只能处理100条，在hql里限制了
    public JsonResult<?> supplement(SupplementOrder order, HttpServletRequest request) {

        logger.debug("supplement order:{}", order.toString());
        JsonResult failure = new JsonResult<>(ErrorCode.ServerError.SERVER_ERROR.getCode());
        JsonResult success = new JsonResult<>(ErrorCode.Success.SUCCESS.getCode());

        if (!verify(order, request)) {
            return failure;
        }

        List list = null;
        switch (SupplementType.get(order.getType())) {
            case Failure_Resend:
                if (!verifyBeginTime(order)) return failure;
                list = orderService.getOrders(SupplementType.Failure_Resend, order.getBeginTime(),
                        0, 0);
                logger.info("Failure_Resend list.size = {}", list.size());
                resend(list);
                break;
            case Success_Resend:
                if (!verifyBeginTime(order)) return failure;
                list = orderService.getOrders(SupplementType.Success_Resend, order.getBeginTime(),
                        order.getAppId(), order.getOrderId());
                logger.info("Success_Resend list.size = {}", list.size());
                resend(list);
                break;
            case ChannelBug:
                if (0 == order.getOrderId()) {
                    logger.error("ChannelBug error: orderId is needed!");
                    return failure;
                }
                TSdkOrder charge = orderService.queueOrder(order.getOrderId());
                if (null == charge) {
                    logger.error("supplement error: orderId not found! orderId = {}", order.getOrderId());
                    return failure;
                }
                SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
                if (null == paramCache) {
                    logger.error("supplement error: packageId not found! packageId = {}", charge.getPackageId());
                    return failure;
                }
                //更新订单参数
                charge.setChannelBillNum(order.getChannelBillNum());
                callbackService.orderHandler(charge, paramCache);
                break;
            default:
                logger.error("supplement error: type not found! type = {}", order.getType());
                return failure;
        }
        return success;
    }

    private boolean verifyBeginTime(SupplementOrder order) {
        long intervalMills = System.currentTimeMillis() - order.getBeginTime();
        if (intervalMills > Constants.ONE_DAY * 365 || intervalMills <= 0) {
            logger.error("verifyBeginTime error: beginTime wrong. interval: {} days",
                    intervalMills / Constants.ONE_DAY);
            return false;
        }
        return true;
    }

    // 目前，黑客如果知道了我们的参数，在我们内网部署服务，获取到我们数据库数据，拼接参数，才能破解这个接口
    // 如果能获取到数据库数据，线上那些服务，也已经能随便发货了
    private boolean verify(SupplementOrder order, HttpServletRequest request) {
        // 限制只有某台服务器能跑服务
        if (!VerifyUitl.getLocalIps().contains(LocalIp)) {
            logger.error("supplement verify error: Wrong server!");
            return false;
        }
        // 限制只能访问本机内网网卡
        String host = request.getHeader("Host");
        if (!host.equals(ServerLocalHost)) {
            logger.error("supplement verify error: Wrong interface!");
            return false;
        }
        if (!order.qualified()) {
            logger.error("supplement verify error: Wrong param!");
            return false;
        }
        if (!order.getSign().equals(orderService.getSupplementSign())) {
            logger.error("supplement verify error: Wrong sign!");
            return false;
        }
        return true;
    }

    private void resend(List list) {
        for (Object tSdkOrder : list) {
            TSdkOrder item = (TSdkOrder) tSdkOrder;
            logger.debug("{}", gson.toJson(item));
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(item.getPackageId()));

            if (null == paramCache) {
                logger.error("supplement error: packageId not found! packageId = {}", item.getPackageId());
                continue;
            }
            callbackService.addToPool(item, paramCache);
        }
    }
}
