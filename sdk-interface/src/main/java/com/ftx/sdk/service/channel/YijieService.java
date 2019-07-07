package com.ftx.sdk.service.channel;

import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.model.YijiePayModel;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.security.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/8/26.
 */
@Service
public class YijieService {

    private Logger logger = LoggerFactory.getLogger(YijieService.class);

    public JsonResult<?> pay(YijiePayModel yijiePayModel, SdkParamCache paramCache) throws IllegalAccessException {

        Map<String, String> param = MapsUtils.object2StringMap(yijiePayModel);

        String srcSign = param.get("sign");
        param.remove("sign");
        String befSign = MapsUtils.createLinkString(param, true, false) + paramCache.channelConfig().get("channelAppSecret");
        String mySign = MD5Util.getMD5(befSign);
        if(srcSign.equalsIgnoreCase(mySign)) {
            return new JsonResult<>(ErrorCode.Success.SUCCESS.getCode(), "success");
        }
        logger.error("our befsign:{}, sign:{}, channel sign:{}", befSign, mySign, srcSign);
        return new JsonResult<>(ErrorCode.RequestError.REQUEST_SIGN_ERROR.getCode(), "sign error");
    }
}
