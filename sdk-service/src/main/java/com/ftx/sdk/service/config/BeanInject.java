package com.ftx.sdk.service.config;

import com.google.gson.Gson;
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

}
