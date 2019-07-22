package com.ftx.sdk.controller;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.sdk.SecurityRequest;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.entity.type.GameSubmitDataType;
import com.ftx.sdk.entity.user.StatisModel;
import com.ftx.sdk.entity.user.SubmitDataModel;
import com.ftx.sdk.exception.SignVerifyException;
import com.ftx.sdk.service.UserService;
import com.google.gson.*;
import org.apache.dubbo.config.annotation.Reference;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by zeta.cai on 2017/7/14.
 */
@RestController
@RequestMapping("user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Reference(version = DubboConstant.VERSION, check = false)
    private UserService service;

    /**
     * 提交数据接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "submit")
    public JsonResult<?> submitData(SecurityRequest request) {
        JsonResult response = new JsonResult<>(ErrorCode.ServerError.SERVER_ERROR.getCode());

        String data = null;
        SubmitDataModel submitData = null;
        try {
            data = request.getData();
            logger.info("/user/submit 入参:data={}", new Gson().toJson(data));
            submitData = new GsonBuilder().registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() {
                @Override
                public Timestamp deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    if (jsonElement.getAsJsonPrimitive().getAsLong() == 0)
                        return null;
                    return new Timestamp(jsonElement.getAsJsonPrimitive().getAsLong() * 1000);
                }
            }).create().fromJson(data, SubmitDataModel.class);
            if (!submitData.qulified()) {
                throw new Exception();
            }
        } catch (Exception e) {
            logger.warn("提交数据接口，请求参数为空或解密失败");
            return response.setCode(ErrorCode.RequestError.REQUEST_SIGN_ERROR.getCode()).setMessage("请求参数错误");
        }

        try {
            if (this.submitData(submitData))
                response.setCode(ErrorCode.Success.SUCCESS.getCode()).setMessage("SUCCESS");
            else
                response.setMessage("api no return");
        } catch (SignVerifyException e) {
            response.setCode(ErrorCode.RequestError.REQUEST_SIGN_ERROR.getCode()).setMessage(e.getMessage());
        } catch (ConstraintViolationException ignored) {
            return response.setMessage("创建角色失败: 角色已存在");
        }

        return response;

    }

    private boolean submitData(SubmitDataModel submitData) throws SignVerifyException {
        //记录数据
        if (submitData.getType() == GameSubmitDataType.ROLE_LOGIN.getType()) {
            //角色登陆
            return service.roleLogin(submitData.getUser(), submitData.getAppId(), submitData.getChannelId(), submitData.getChannelUserId());
        } else if (submitData.getType() == GameSubmitDataType.CREATE_ROLE.getType()) {
            //创建角色
            return service.createUserRole(submitData.getUser(), submitData.getAppId(), submitData.getChannelId(), submitData.getChannelUserId());
        } else if (submitData.getType() == GameSubmitDataType.ROLE_LEVEL_UP.getType()) {
            //角色升级
        } else if (submitData.getType() == GameSubmitDataType.ROLE_LOGOUT.getType()) {
            //角色注销
        }
        return true;
    }

    /**
     * 实时数据接口，记录实时在线用户，由cp主动调用
     *
     * @param sign
     * @return
     */
    @RequestMapping(value = "onlineNum")
    public JsonResult<?> statistics(@Validated StatisModel statisInfo, String sign, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder message = new StringBuilder();
            for (ObjectError error : errors) {
                message.append(error.getDefaultMessage()).append(",");
            }
            return new JsonResult<>(ErrorCode.RequestError.REQUEST_PARAMETER_NULL.getCode()).setMessage(message.toString());
        }

        try {
            service.subStatis(statisInfo, sign);
        } catch (SignVerifyException e) {
            return new JsonResult<>(ErrorCode.RequestError.REQUEST_SIGN_ERROR.getCode(), e.getMessage());
        }
        return new JsonResult<>(ErrorCode.Success.SUCCESS.getCode(), "SUCCESS");
    }
}
