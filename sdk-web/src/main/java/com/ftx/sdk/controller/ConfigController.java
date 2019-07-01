package com.ftx.sdk.controller;

import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.entity.sdk.result.SecurityResponse;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.VerifyUitl;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zeta.cai on 2017/7/11.
 */
@RestController
@RequestMapping("config")
public class ConfigController {
    Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Reference(version = DubboConstant.VERSION,check = false)
    private SDKService sdkService;

    @Autowired
    private Gson gson;

   /* @Autowired
    ComboPooledDataSource comboPooledDataSource;*/

    /**
     * 客户端初始化
     * @param packageId 包Id
     * @return app、channel、package的联合参数
     */
    @RequestMapping(value = "init")
    public String init(@RequestParam int packageId) throws Exception {

        logger.debug("/config/init接口开始");

        /*logger.debug("/config/init的c3p0连接池状态：正在使用线程数={},空闲的线程数={},总连接数={}",
                comboPooledDataSource.getNumBusyConnections(),comboPooledDataSource.getNumIdleConnections(),comboPooledDataSource.getNumConnections());*/

        String response = gson.toJson(new JsonResult<>(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode(), "packageId incorrect"));

        if (!VerifyUitl.verifyPackageId(packageId)) {
            return SecurityResponse.toSecurity(response);
        }

        //执行查询逻辑
        SdkParamCache sdkParamConfig = sdkService.getConfig(packageId);
        if (sdkParamConfig != null){
            //确保Map类型的ChannelAPI参数已经加载
            sdkParamConfig.channelConfig();

            response = gson.toJson(new JsonResult<>(ErrorCode.Success.SUCCESS.getCode(),"success", sdkParamConfig));
        }

        logger.debug("/config/init接口结束");

        return SecurityResponse.toSecurity(response);
    }
}
