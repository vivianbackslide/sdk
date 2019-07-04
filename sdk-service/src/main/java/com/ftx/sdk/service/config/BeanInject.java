package com.ftx.sdk.service.config;

import com.ftx.sdk.login.LoginHandlerManager;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zeta.cai on 2017/6/26.
 */
@Configuration
public class BeanInject {

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean(initMethod = "initLoginHandler")
    public LoginHandlerManager loginHandlerManager() {
        return new LoginHandlerManager();
    }

    @Bean
    public JsonParser jsonParser() {
        return new JsonParser();
    }


}
