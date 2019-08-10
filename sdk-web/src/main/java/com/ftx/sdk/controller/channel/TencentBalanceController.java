package com.ftx.sdk.controller.channel;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.model.TencentPayModel;
import com.ftx.sdk.service.channel.TencentPayService;
import com.ftx.sdk.utils.tencent.TencentResponseUtil;
import com.ftx.sdk.utils.tencent.TencentUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lei.nie on 2016/7/15.
 */
@RestController
public class TencentBalanceController {
    private Logger logger = LoggerFactory.getLogger(TencentBalanceController.class);
    @Reference(version = DubboConstant.VERSION, check = false)
    private TencentPayService tencentPayService;
    @Autowired
    private JsonParser jsonParser;
    @Autowired
    private Gson gson;

    @RequestMapping(value = "/charge/tencent/query_balance")
    public String callback(@Validated TencentPayModel payModel, BindingResult bindingResult) {
        logger.info("/charge/tencent/query_balance 入参:payModel-{}, bindingResult-{}", payModel, bindingResult);
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder message = new StringBuilder();
            for (ObjectError error : errors) {
                message.append(error.getDefaultMessage()).append(",");
            }
            return TencentResponseUtil.create(-1, message.toString());
        }

        try {
            String response = tencentPayService.queryBalance(payModel);
            JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
            int code = jsonObject.get("ret").getAsInt();
            String message = TencentUtil.getMessageByCode(code);
            long balance = code == 0 ? jsonObject.get("balance").getAsLong() : 0;
            return TencentResponseUtil.create(code, message, balance);
        } catch (Exception e) {
            logger.error("tencent query balance error, {}, {}", e.getMessage(), gson.toJson(payModel));
            return TencentResponseUtil.create(-1, e.getMessage(), 0);
        }
    }
}
