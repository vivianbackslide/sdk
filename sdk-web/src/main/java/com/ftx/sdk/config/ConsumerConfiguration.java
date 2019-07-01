package com.ftx.sdk.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 描述：dubbo配置类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-10 10:01
 */
@Configuration
@PropertySource(value = "classpath:dubbo.properties")
@EnableDubbo(scanBasePackages = "com.ftx.sdk")
@ComponentScan(value = "com.ftx.sdk")
public class ConsumerConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Bean
    public JsonParser jsonParser() {
        return new JsonParser();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}

