package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.model.TencentPayModel;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.service.channel.TencentPayService;
import com.ftx.sdk.utils.tencent.TencentResponseUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lei.nie on 2016/7/15.
 */
@RestController
public class TencentPayController {
    @Reference(version = DubboConstant.VERSION,check = false)
    private TencentPayService service;
    @Reference(version = DubboConstant.VERSION,check = false)
    private OrderService orderService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private SDKService sdkService;

    @RequestMapping(value = "/charge/tencent/pay")
    public String callback(@Validated TencentPayModel payModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder message = new StringBuilder();
            for (ObjectError error : errors) {
                message.append(error.getDefaultMessage()).append(",");
            }
            return TencentResponseUtil.create(-1, message.toString(), payModel.getBillno());
        }

        //支付流程中使用订单记录的金额
        String orderId = payModel.getBillno();
        TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderId));
        SdkParamCache configCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
        int currencyRate = Integer.parseInt(configCache.channelConfig("currencyRate"));
        payModel.setAmt(charge.getAmount() * currencyRate / 100);
        //支付
        service.pay(payModel);

        return TencentResponseUtil.create(0, "SUCCESS");
    }
}

