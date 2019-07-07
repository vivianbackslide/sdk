package com.ftx.sdk.service.channel;

import com.ftx.sdk.entity.orm.TSdkOrder;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.xiaomi.XiaoMiPayModel;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.security.HmacSHA1Encryption;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author zhenbiao.cai
 * @date 2016/6/27.
 */
@Service
public class XiaoMiService {

    @Autowired
    private Gson gson;

    public JsonResult<?> pay(XiaoMiPayModel payModel, SdkParamCache paramCache, TSdkOrder charge) throws Exception {

        String orderQueryUrl = paramCache.getChannelDomain() + paramCache.channelAPI().get("api_order_query");
        Map<String, String> orderQueryMap = Maps.newHashMap();
        orderQueryMap.put("appId", payModel.getAppId());
        orderQueryMap.put("cpOrderId", payModel.getCpOrderId());
        orderQueryMap.put("uid", payModel.getUid());

        String befSign = MapsUtils.createLinkString(orderQueryMap, true, false);
        orderQueryMap.put("signature", HmacSHA1Encryption.HmacSHA1Encrypt(befSign, paramCache.channelConfig().get("channelAppSecret")));

        String result = HttpTools.doGet(orderQueryUrl, orderQueryMap);
        XiaoMiPayModel xiaoMiPayModel = gson.fromJson(result, XiaoMiPayModel.class);
        if(charge.getAmount() == Integer.parseInt(xiaoMiPayModel.getPayFee())) {
            return new JsonResult<Object>(ErrorCode.Success.SUCCESS.getCode());
        }
        else {
            return new JsonResult<Object>(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode(), "query order error");
        }
    }
}
