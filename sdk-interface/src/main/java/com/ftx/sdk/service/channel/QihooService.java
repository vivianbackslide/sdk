package com.ftx.sdk.service.channel;

import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.QihooPayModel;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by lei.nie on 2016/7/11.
 */

@SuppressWarnings("SpellCheckingInspection")
@Service
public class QihooService {
    private Logger logger = LoggerFactory.getLogger(QihooService.class);

    public JsonResult<?> pay(QihooPayModel payModel, SdkParamCache paramCache) {
        String appSecret  = paramCache.channelConfig().get("channelAppSecret");
        if (Strings.isNullOrEmpty(appSecret)) {
            return new JsonResult<>(ErrorCode.ServerConfError.APP_CHANNEL_CONF_SECRET_NULL.getCode(), "appSecret is null");
        }
        try {
            String sign = payModel.getSign();
            String flag = payModel.getGateway_flag();
            if(flag.equals("success")){
                String str = payModel.toSortString() + "#" + appSecret;
                String checkSign = MD5Util.getMD5(str);
                if(sign.equals(checkSign)){
                    return new JsonResult<Object>(ErrorCode.Success.SUCCESS.getCode(), "success");
                }
                else{
                    return new JsonResult<Object>(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode(), "sign error");
                }
            }
            else{
                return new JsonResult<Object>(ErrorCode.RequestError.REQUEST_ORDER_FAILED.getCode(), "order not success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("360_pay_callback计算签名时异常:{}", e.getMessage());
            return new JsonResult<Object>(ErrorCode.ServerError.SERVER_ERROR.getCode(), "make sign error");
        }
    }
}
