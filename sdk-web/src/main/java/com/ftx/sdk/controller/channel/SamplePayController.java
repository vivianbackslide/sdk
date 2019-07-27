package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/6/25.
 */
@RestController
public class SamplePayController {

    private Logger logger = LoggerFactory.getLogger(SamplePayController.class);

    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;

    @RequestMapping(value = "/charge/sample")
    public String callback(String orderid, String exInfo) {
        try {
            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderid));
            if (null == charge) {
                logger.error("sample_pay_callback接口异常: [获取订单数据失败, orderId={}]", orderid);
                return FAILED;
            }

            //校验
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            Map<String, String> channelConfig = paramCache.channelConfig();

            //判断订单金额相符，若渠道没返回金额则跳过

            //更新订单参数
            charge.setChannelBillNum(orderid);
//            if (charge.getAmount() != MoneyUtil.yuan2fen(ouwanPayModel.getAmount())) { //判断订单金额相符，若渠道没返回金额则跳过
//                orderService.illegalAmountHandler(charge);
//                logger.error("sample_pay_callback 接口异常: [订单金额异常: order:{}, channelOrder:{}]",
//                        gson.toJson(charge), orderid);
//                return SUCCESS;
//            }

            //执行统一逻辑，包括入库和通知游戏
            callbackService.orderHandler(charge, paramCache);

            return SUCCESS;

        } catch (Exception e) {
            logger.error("sample_pay_callback接口异常: [服务器异常:{}]", e.getMessage(), e);
            return FAILED;
        }
    }
}
