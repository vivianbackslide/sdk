package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.MoneyUtil;
import com.ftx.sdk.utils.huawei.CommonUtil;
import com.ftx.sdk.utils.huawei.SignUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lei.nie on 2016/7/12.
 */

@RestController
public class HuaweiPayController {
    private static final String PAY_SUCCEED = "0";
    private static final int CHECK_SIGN_SUCCEED = 0;
    private static final int CHECK_SIGN_FAILED = 1;
    private static final int TIME_OUT = 2;
    private static final int BUSINESS_ERROR = 3;
    private static final int SYSTEM_ERROR = 94;
    private static final int IO_ERROR = 95;
    private static final int INVALID_URL = 96;
    private static final int INVALID_RESPONSE = 97;
    private static final int INVALID_PARAMS = 98;
    private static final int OTHER_ERROR = 99;

    private Logger logger = LoggerFactory.getLogger(HuaweiPayController.class);

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION,check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION,check = false)
    private OrderService orderService;

    @RequestMapping(value = "/charge/huawei")
    public String callback(HttpServletRequest request) {
        String str = "";
        InputStream stream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            String line = null;
            StringBuffer sb = new StringBuffer();
            request.setCharacterEncoding("UTF-8");
            stream = request.getInputStream();
            isr = new InputStreamReader(stream);
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            br.close();
            str = sb.toString();
            logger.debug("huawei pay callback:{}", str);

            // 1.检查参数有效性
            Map<String, Object> map = getValue(str);
            if (map == null || map.isEmpty()) {
                logger.error("huawei_pay_callback invalid request:{}", str);
                return createResponse(INVALID_PARAMS);
            }

            String orderId = (String) map.get("requestId");
            // 获取订单
            TSdkOrder charge = orderService.queueOrder(Long.parseLong(orderId));
            if (null == charge) {
                logger.error("huawei_pay_callback接口异常: [获取缓存数据失败, requestBody={}, orderId={}]", str, orderId);
                return createResponse(OTHER_ERROR);
            }

            //更新订单参数
            charge.setChannelBillNum(String.valueOf(orderId));

            // 4.调用服务方法进行支付
            SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(charge.getPackageId()));
            String sign = (String) map.get("sign");
            String payPublicKey = paramCache.channelConfig().get("publicKey");
            boolean verifySucceed = CommonUtil.rsaDoCheck(map, sign, payPublicKey);

            // 6.判断回调验证是否成功，成功则进行异步发货
            if (!verifySucceed) {
                Map<String, String> data = MapsUtils.string2map(str);
                if (data.containsKey("sysReserved")) {
                    data.put("sysReserved", URLDecoder.decode(data.get("sysReserved"), "utf-8"));
                }
                if (data.containsKey("extReserved")) {
                    data.put("extReserved", URLDecoder.decode(data.get("extReserved"), "utf-8"));
                }
                String sign2 = URLDecoder.decode(data.get("sign"), "utf-8");
                data.remove("signType");
                data.remove("sign");
                String content = MapsUtils.createLinkString(data, true, false);
                boolean verify = SignUtils.verify(content, payPublicKey, sign2);
                if (verify) {
                    String result = (String) map.get("result");
                    if (result.equals(PAY_SUCCEED)) {
                        //执行统一逻辑，包括入库和通知游戏
                        callbackService.orderHandler(charge, paramCache);
                    }
                    return createResponse(CHECK_SIGN_SUCCEED);
                }
                orderService.VerifyFailed(charge);
                logger.error("huawei_pay_callback verify failed:[requestData={}]", str);
                return createResponse(CHECK_SIGN_FAILED);
            } else if (charge.getAmount() != MoneyUtil.yuan2fen((String) map.get("amount"))) {    //判断订单金额相符，若渠道没返回金额则跳过
                orderService.illegalAmountHandler(charge);
                logger.error("huawei_pay_callback接口异常: [订单金额异常: order:{}, channelOrder:{}]", gson.toJson(charge), map.toString());
                return createResponse(CHECK_SIGN_SUCCEED);
            } else {
                String result = (String) map.get("result");
                if (result.equals(PAY_SUCCEED)) {
                    logger.info("huawei订单成功");

                    //执行统一逻辑，包括入库和通知游戏
                    callbackService.orderHandler(charge, paramCache);
                }
                return createResponse(CHECK_SIGN_SUCCEED);
            }
        } catch (Exception e) {
            logger.error("huawei_pay_callback接口异常: [服务器异常: requestBody={}, error_message={}]", e.getMessage());
            return createResponse(OTHER_ERROR);
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                }
                if (null != stream) {
                    stream.close();
                }
            } catch (Exception e) {
                logger.error("huawei_pay_callback接口异常 stream close error : {}", e.getMessage(), e);
            }
        }
    }

    public Map<String, Object> getValue(String str) {
        Map<String, Object> valueMap = new HashMap<String, Object>();
        if (null == str || "".equals(str)) {
            return valueMap;
        }

        String[] valueKey = str.split("&");
        for (String temp : valueKey) {
            String[] single = temp.split("=");
            valueMap.put(single[0], single[1]);
        }

        // 接口中，如下参数sign和extReserved、sysReserved是URLEncode的，所以需要decode，其他参数直接是原始信息发送，不需要decode
        try {
            String sign = (String) valueMap.get("sign");
            String extReserved = (String) valueMap.get("extReserved");
            String sysReserved = (String) valueMap.get("sysReserved");

            if (null != sign) {
                sign = URLDecoder.decode(sign, "utf-8");
                valueMap.put("sign", sign);
            }
            if (null != extReserved) {
                extReserved = URLDecoder.decode(extReserved, "utf-8");
                valueMap.put("extReserved", extReserved);
            }

            if (null != sysReserved) {
                sysReserved = URLDecoder.decode(sysReserved, "utf-8");
                valueMap.put("sysReserved", sysReserved);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueMap;
    }

    private String createResponse(int code) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", code);
        return jsonObject.toString();
    }
}
