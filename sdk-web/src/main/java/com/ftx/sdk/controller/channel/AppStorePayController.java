package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.AppStorePayModel;
import com.ftx.sdk.model.AppStoreResultFieldsModel;
import com.ftx.sdk.service.channel.CallbackService;
import com.ftx.sdk.service.channel.OrderService;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by zeta.cai on 2017/2/27.
 */
@RestController
//@RequestMapping(consumes = "application/json")
public class AppStorePayController {
    private Logger logger = LoggerFactory.getLogger(AppStorePayController.class);

    @Autowired
    private Gson gson;
    @Reference(version = DubboConstant.VERSION, check = false)
    private CallbackService callbackService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;
    @Reference(version = DubboConstant.VERSION, check = false)
    private OrderService orderService;

    private static final String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    private static final String Pay_URL = "https://buy.itunes.apple.com/verifyReceipt";

    @RequestMapping(value = "/charge/AppStore")
    public JsonResult callback(AppStorePayModel payInfo) {
        logger.info("new apple order : {}", gson.toJson(payInfo));
        //校验参数
        String sign = MD5Util.getMD5("orderId=" + payInfo.getOrderId() + "&channelOrderId=" + payInfo.getChannelOrderId() + "&receipt=" + payInfo.getReceipt() + "ftxAppStore");
        if (!sign.equalsIgnoreCase(payInfo.getSign())){
            logger.error("AppStore订单失败:[非法订单, 参数校验失败 orderId={}]", payInfo.getOrderId());
            return new JsonResult<>(ErrorCode.ServerError.SERVER_ERROR.getCode(), "非法订单");
        }
        //处理订单
        TSdkOrder order = orderService.queueOrder(Long.parseLong(payInfo.getOrderId()));
        if (order == null){
            logger.error("AppStore订单失败:[查不到订单 orderId={}]", payInfo.getOrderId());
            return new JsonResult<>(ErrorCode.ServerError.SERVER_ERROR.getCode(), "查不到订单");
        }

        SdkParamCache paramCache = sdkService.getConfig(Integer.parseInt(order.getPackageId()));

        ArrayList<AppStoreResultFieldsModel> fields = null;

        try {
            fields = verifyReceipt(Pay_URL, payInfo.getReceipt());

            //由于收据中可能包含多个订单的信息，判断苹果收据中订单是否与客户端返回一致
            boolean isFound = false;
            for (AppStoreResultFieldsModel field : fields) {
                if (field.getTransaction_id().equals(payInfo.getChannelOrderId())){
                    isFound = true;
                    break;
                }
            }
            if (!isFound){
                logger.error("AppStore订单失败:[收据无对应订单 orderId={}]", payInfo.getOrderId());
                return new JsonResult<>(ErrorCode.ServerError.SERVER_ERROR.getCode(), "收据无对应订单");
            }

            //更新单号
            order.setChannelBillNum(payInfo.getChannelOrderId());

            //向游戏异步发送请求
            callbackService.orderHandler(order, paramCache);
        } catch (Exception e) {
            logger.error("request apple server error:{}", e.getMessage(), e);
            return new JsonResult<>(ErrorCode.ServerError.SERVER_ERROR.getCode(), e.getMessage());
        }

        return new JsonResult<>(ErrorCode.Success.SUCCESS.getCode(), "success");
    }

    public ArrayList<AppStoreResultFieldsModel> verifyReceipt(String url, String receipt) throws Exception {
        String response = HttpTools.doPostByJson(url, "{\"receipt-data\": \"" + receipt + "\"}");
        RequestResult result = gson.fromJson(response, RequestResult.class);
        if (result == null) {
            throw new Exception("request apple server error");
        }else if (result.status == 21007) {
            return verifyReceipt(SANDBOX_URL, receipt);
        }else if (result.status != 0){
            throw new Exception("receipt invalid");
        }
        return result.receipt.in_app;
    }

    class RequestResult {
        int status;
        String environment;
        Receipt receipt;
    }

    class Receipt {
        ArrayList<AppStoreResultFieldsModel> in_app;
    }
}
