package com.ftx.sdk.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 描述：初始化
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-16 22:33
 */
@Component
public class SystemInitialization {

    private static Logger logger = LoggerFactory.getLogger(SystemInitialization.class);

   /* @Autowired
    private SystemConfigOnDBService systemConfigOnDBService;*/




    @PostConstruct
    public void init(){
        try{
            logger.info("初始化系统配置");
            //systemConfigOnDBService.initSystemConfig();

        }catch (Exception e){
           logger.error("初始化程序失败：",e);
        }
    }

}
