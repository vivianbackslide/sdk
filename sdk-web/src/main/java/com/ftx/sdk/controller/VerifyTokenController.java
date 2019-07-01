package com.ftx.sdk.controller;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ServerException;
import com.ftx.sdk.exception.SignVerifyException;
import com.ftx.sdk.service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zeta.cai on 2017/8/11.
 */
@RestController
public class VerifyTokenController {

    private Logger logger = LoggerFactory.getLogger(VerifyTokenController.class);

    @Reference(version = DubboConstant.VERSION,check = false)
    private UserService service;
    @Autowired
    private Gson gson;

    /*@Autowired
    ComboPooledDataSource comboPooledDataSource;*/

    /**
     * 登陆校验，cp主动调用
     * @param loginInfo 数据集
     * @return User
     */
    @RequestMapping("/verifyToken")
    public JsonResult<?> checkLogin(@Validated LoginInfo loginInfo, BindingResult bindingResult) throws SQLException {

        logger.debug("/verifyToken接口开始");

       /* logger.debug("/verifyToken的c3p0连接池状态：正在使用线程数={},空闲的线程数={},总连接数={}",
                comboPooledDataSource.getNumBusyConnections(),comboPooledDataSource.getNumIdleConnections(),comboPooledDataSource.getNumConnections());*/

        JsonResult<User> response = new JsonResult<>(ErrorCode.ServerError.SERVER_ERROR.getCode());

        if (bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder message = new StringBuilder();
            for (ObjectError error : errors){
                message.append(error.getDefaultMessage()).append(",");
            }
            logger.error("登陆异常:[请求参数不完整, packageId:{}, message:{}]", loginInfo.getPackageId(), message.toString());
            return response.setCode(ErrorCode.RequestError.REQUEST_PARAMETER_NULL.getCode()).setMessage(message.toString());
        }

        try {
            //执行登陆校验逻辑
            User user = service.checkLoginHandle(loginInfo);
            if (null != user) {
                response.setCode(ErrorCode.Success.SUCCESS.getCode()).setMessage("SUCCESS").setData(user);
//                logger.info("登录成功:[appId={}, channelId={}, channelName={}, userId={}]",loginInfo.getAppId(), user.getChannelId(), user.getChannelName(), user.getUserId());
            }else
                response.setCode(ErrorCode.APIResultError.INTERFACE_FAILED_RESULT.getCode()).setMessage("checkLogin fail");
        }catch (JsonSyntaxException e){
            response.setCode(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode()).setMessage("data serialized Exception");
        }catch (SignVerifyException e) {
            response.setCode(ErrorCode.RequestError.REQUEST_SIGN_ERROR.getCode()).setMessage(e.getMessage());
        }catch (ServerException e) {
            response.setCode(ErrorCode.ServerError.SERVER_ERROR.getCode()).setMessage(e.getMessage());
        }

        if (!response.success())
            logger.warn("登陆异常:[data:{}, reason:{}]", gson.toJson(loginInfo), response.getMessage());

        logger.debug("/verifyToken接口结束");

        return response;
    }
}
