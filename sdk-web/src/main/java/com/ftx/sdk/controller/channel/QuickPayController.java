package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.MoneyUtil;
import com.ftx.sdk.utils.quick.DesUtil;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * Created by jiteng.huang on 2016/9/2.
 */
@RestController
public class QuickPayController {

    private Logger logger = LoggerFactory.getLogger(QuickPayController.class);

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";

    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;
    @Autowired
    private Gson gson;

    @RequestMapping(value = "/charge/quick/*")
    public String callback(@RequestParam String nt_data, String sign, String md5Sign, HttpServletRequest request) {
        try {
            logger.debug("quick nt_data:{}, sign:{}, md5Sign:{}", nt_data, sign, md5Sign);
            logger.info("quick nt_data:{}, sign:{}, md5Sign:{}", nt_data, sign, md5Sign);
            // 1.检查字符串参数是否为空
            if (nt_data == null || sign == null || md5Sign == null) {
                logger.error("quick_pay_callback接口异常: [参数验证失败, requestBody={}, error_message={}]", null, "参数为空");
                return FAILED;
            }
            // 2.检查参数有效性
            String pathInfo = request.getPathInfo();
            int index = pathInfo.lastIndexOf("/");
            int packageId = Integer.parseInt(pathInfo.substring(index + 1));
            // 获取订单
            TSdkOrder charge = orderService.queueOrder(packageId);
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            String callbackKey = paramCache.channelConfig().get("callbackKey");
            String md5SignLocal = MD5Util.getMD5(nt_data + sign + callbackKey);
            if (!md5Sign.equals(md5SignLocal)) {
                logger.error("quick_pay_callback接口异常: [签名验证失败, md5Sign={}, md5SignLocal={}]", md5Sign, md5SignLocal);
                return FAILED;
            }
            // 3.获取存在缓存的透传（自定义）参数
            // 4.检查透传参数
            nt_data = URLDecoder.decode(nt_data,"utf-8");
            String xml = DesUtil.decode(nt_data, callbackKey);
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            Element message = root.element("message");
            Element game_order = message.element("game_order");
            Element amount = message.element("amount");
            Element order_no = message.element("order_no");
            String orderId = game_order.getStringValue();
            String channelAmount = amount.getStringValue();
            String channelOrderNo = order_no.getStringValue();

            // 7.判断回调验证是否成功，成功则进行异步发货
            //更新订单参数
            charge.setChannelBillNum(channelOrderNo);
            if (charge.getAmount() != MoneyUtil.yuan2fen(channelAmount)) { //判断订单金额相符，若渠道没返回金额则跳过
                orderService.illegalAmountHandler(charge);
                logger.error("quick_pay_callback 接口异常: [订单金额异常: order:{}, channelOrder:{}]",
                        gson.toJson(charge), channelOrderNo);
                return SUCCESS;
            }

            //执行统一逻辑，包括入库和通知游戏
            callbackService.orderHandler(charge, paramCache);
            return SUCCESS;

        } catch (Exception e) {
            logger.error("quick_pay_callback接口异常: [服务器异常: error_message={}]", e.getMessage(), e);
            return FAILED;
        }
    }


}
