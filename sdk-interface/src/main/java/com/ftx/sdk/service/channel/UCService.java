package com.ftx.sdk.service.channel;

import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.UCPayModel;
import com.ftx.sdk.model.UCPayModelData;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author zhenbiao.cai
 * @date 2016/6/21.
 */
@Service
public class UCService {

    private Logger logger = LoggerFactory.getLogger(UCService.class);

    public JsonResult<?> pay(UCPayModel payModel, SdkParamCache paramCache) {
        if (!payModel.getData().getOrderStatus().equalsIgnoreCase("S")) {
            return new JsonResult<Object>(ErrorCode.RequestError.REQUEST_ORDER_FAILED.getCode(), "order status failed");
        }
        String channelAppKey = paramCache.channelConfig().get("channelAppKey");
        // 计算签名通过之后就算验证成功，失败的订单另行处理
        String befSign = makeSign(payModel.getData(), channelAppKey);
        String sign = MD5Util.getMD5(befSign);
        if (payModel.getSign().equalsIgnoreCase(sign)) {
            return new JsonResult<Object>(ErrorCode.Success.SUCCESS.getCode(), "success");
        } else {
            String errorMsg = String.format("sign error, our bef sign:%s, sign:%s", befSign, sign);
            logger.error(errorMsg);
            return new JsonResult<Object>(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode(), errorMsg);
        }
    }

    private String makeSign(UCPayModelData data, String appSecret) {
        StringBuilder src = new StringBuilder();
        src.append("accountId=").append(data.getAccountId());
        src.append("amount=").append(data.getAmount());
        src.append("callbackInfo=").append(data.getCallbackInfo());
        if (!Strings.isNullOrEmpty(data.getCpOrderId()))
            src.append("cpOrderId=").append(data.getCpOrderId());
        src.append("creator=").append(data.getCreator());
        src.append("failedDesc=").append(data.getFailedDesc());
        src.append("gameId=").append(data.getGameId());
        src.append("orderId=").append(data.getOrderId());
        src.append("orderStatus=").append(data.getOrderStatus());
        src.append("payWay=").append(data.getPayWay());
        src.append(appSecret);
        return src.toString();
    }
}
