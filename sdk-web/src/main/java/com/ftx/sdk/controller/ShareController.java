package com.ftx.sdk.controller;

import com.ftx.sdk.model.FacebookGiftModel;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.MapsUtils;
import com.google.gson.Gson;
import org.apache.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 海外服facebook分享发礼包
 */
@RestController
@RequestMapping("/share")
public class ShareController {

    Logger logger = LoggerFactory.getLogger(ShareController.class);
    private static final String FAILED = "FAILURE";

    @Autowired
    private Gson gson;

    @RequestMapping("/gift")
    public String sendGift(FacebookGiftModel facebookGiftModel) {
        try {
            if (StringUtils.isAnyEmpty(facebookGiftModel.getUid(), facebookGiftModel.getGiftNo(), facebookGiftModel.getRoleInfo(), facebookGiftModel.getServerInfo(), facebookGiftModel.getSign())) {
                return FAILED;
            }
            logger.info("facebook gift param:{}", gson.toJson(facebookGiftModel));
            String result = HttpTools.doPost("http://web.ft.t4game.com/fbkey/exchange", MapsUtils.object2StringMap(facebookGiftModel));
            logger.info("facebook gift result:{}", result);
            return result;
        } catch (Exception e) {
            logger.error("facebook 分享发送礼包接口异常]", e.getMessage());
            return FAILED;
        }
    }

}
