package com.ftx.sdk.controller;

import com.alibaba.fastjson.JSON;
import com.ftx.sdk.common.constants.DubboConstant;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.sdk.result.ErrorCode;
import com.ftx.sdk.entity.sdk.result.JsonResult;
import com.ftx.sdk.entity.sdk.result.SecurityResponse;
import com.ftx.sdk.service.channel.SDKService;
import com.ftx.sdk.utils.VerifyUitl;
import com.ftx.sdk.vo.ConfigVO;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by zeta.cai on 2017/7/11.
 */
@RestController
@RequestMapping("config")
public class ConfigController {
    Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Reference(version = DubboConstant.VERSION, check = false)
    private SDKService sdkService;

    @Autowired
    private Gson gson;

   /* @Autowired
    ComboPooledDataSource comboPooledDataSource;*/

    /**
     * 客户端初始化
     *
     * @param packageId 包Id
     * @return app、channel、package的联合参数
     */
    @RequestMapping(value = "init")
    public String init(@RequestParam int packageId) throws Exception {

        logger.debug("/config/init接口开始");
        logger.info("/config/init 入参:packageId={}", packageId);
        /*logger.debug("/config/init的c3p0连接池状态：正在使用线程数={},空闲的线程数={},总连接数={}",
                comboPooledDataSource.getNumBusyConnections(),comboPooledDataSource.getNumIdleConnections(),comboPooledDataSource.getNumConnections());*/

        String response = gson.toJson(new JsonResult<>(ErrorCode.RequestError.REQUEST_PARAMETER_ERROR.getCode(), "packageId incorrect"));

        if (!VerifyUitl.verifyPackageId(packageId)) {
            return SecurityResponse.toSecurity(response);
        }

        //执行查询逻辑
        SdkParamCache sdkParamConfig = sdkService.getConfig(packageId);
        if (sdkParamConfig != null) {
            //确保Map类型的ChannelAPI参数已经加载
            sdkParamConfig.channelConfig();

            ConfigVO configVO = new ConfigVO();
            configVO.setAppId(sdkParamConfig.getAppId());
            configVO.setCallBackUrl(sdkParamConfig.getCallBackUrl());
            configVO.setChannelLabel(sdkParamConfig.getChannelLabel());
            configVO.setPackageId(sdkParamConfig.getPackageId());

            Map<String, String> map = JSON.parseObject(sdkParamConfig.getChannelConfig(), Map.class);
            configVO.setChannelConfig(map);


            //response = gson.toJson(new JsonResult<>(ErrorCode.Success.SUCCESS.getCode(),"success", sdkParamConfig));

            response = com.alibaba.fastjson.JSON.toJSONString(new JsonResult<>(ErrorCode.Success.SUCCESS.getCode(), "success", configVO));
        }

        logger.debug("/config/init接口结束");

        return SecurityResponse.toSecurity(response);
    }
}
