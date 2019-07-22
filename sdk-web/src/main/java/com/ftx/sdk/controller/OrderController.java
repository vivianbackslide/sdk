package com.ftx.sdk.controller;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SecurityRequest;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.service.channel.OrderService;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zeta.cai on 2017/7/21.
 */
@RestController
@RequestMapping("order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService service;

    @Autowired
    private Gson gson;

    @RequestMapping("createOrder")
    public JsonResult createOrder(SecurityRequest request) {
        JsonResult<Long> response = new JsonResult<>(ErrorCode.ServerError.SERVER_ERROR.getCode());

        String order = null;
        TSdkOrder clientCharge = null;
        try {
            order = request.getData();
            logger.info("/order/createOrder 入参:order={}", order);
            clientCharge = gson.fromJson(order, TSdkOrder.class);
            if (!clientCharge.qualified()) {
                throw new Exception();
            }
        } catch (Exception e) {
            logger.warn("提交数据接口，请求参数为空或解密失败");
            return response.setCode(ErrorCode.RequestError.REQUEST_SIGN_ERROR.getCode()).setMessage("请求参数错误");
        }

        try {
            TSdkOrder charge = service.createNewOrder(clientCharge);
            if (charge != null)
                response.setCode(ErrorCode.Success.SUCCESS.getCode()).setMessage("SUCCESS").setData(charge.getOrderId());
        } catch (ConstraintViolationException ignored) {
            response.setCode(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode()).setMessage("创建订单失败，订单Id重复");
        }

        return response;
    }
}
