/*
 * Copyright(c) 2017-2020, 深圳市链融科技股份有限公司
 * Beijing Xiwei Technology Co., Ltd.
 * All rights reserved.
 */
package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.model.MiquwanPayModel;
import com.ftx.sdk.model.xiaomi.XiaoMiResult;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author 83454
 * @version 1.0
 * @date 2020/9/18
 */
public class MiquwanPayController {

    private Logger logger = LoggerFactory.getLogger(MiquwanPayController.class);

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;

    @RequestMapping(value = "/charge/miquwan")
    public String callback(@ModelAttribute MiquwanPayModel miquwanPayModel) {
        try {
            logger.debug("miquwan charge data:{}", gson.toJson(miquwanPayModel));
            logger.info("miquwan charge data:{}", gson.toJson(miquwanPayModel));
            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(miquwanPayModel.getOrderId()));
            if (null == charge) {
                logger.error("miquwan_pay_callback接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", gson.toJson(miquwanPayModel), miquwanPayModel.getOrderId());
                return gson.toJson(new XiaoMiResult(1515, "cpOrderId 错误"));
            }
            //更新订单参数
            charge.setChannelBillNum(miquwanPayModel.getOrderId());
            // 判断订单金额相符，若渠道没返回金额则跳过
            if (charge.getAmount() != Integer.valueOf(miquwanPayModel.getAmount())) {
                orderService.illegalAmountHandler(charge);
                logger.error("miquwan_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), gson.toJson(miquwanPayModel));
                return "FAILURE";
            }
            // 4.调用服务方法进行支付
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            String channelAppKey = paramCache.channelConfig().get("channelAppKey");

            Map<String, String> map = MapsUtils.object2StringMap(miquwanPayModel);
            map.remove("sign");

            //将map拼接成a=avalue&b=bvalue&.....&z=zvalue&key，因为juyou的sdk文档已说需要以参数的key名字母顺序（升序）排，所以第二个参数为true
            String signStr = MapsUtils.createLinkString(map, true, false);
            signStr = signStr + "&key=" + channelAppKey;
            logger.info("米趣玩原始签名sign = {}", signStr);
            String mySign = MD5Util.getMD5(signStr);

            // 6.判断回调验证是否成功，成功则进行异步发货
            //比较游戏传来的sign和自己拼接传来的sign是否相同，相同则表示没有被修改，可以发货
            if (miquwanPayModel.getSign().equals(mySign)) {
                //执行统一逻辑，包括入库和通知游戏
                callbackService.orderHandler(charge, paramCache);
                return "SUCCESS";
            } else {
                logger.error("miquwan_pay_callback接口异常:[requestData={}]", gson.toJson(miquwanPayModel));
                return "FAILURE";
            }
        } catch (Exception e) {
            logger.error("miquwan_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", gson.toJson(miquwanPayModel), e.getMessage(), e);
        }
        return "FAILURE";
    }
}
