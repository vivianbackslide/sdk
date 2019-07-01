package com.ftx.sdk.annotation;

import java.lang.annotation.*;

/**
 * 渠道商标志
 * @author zhenbiao.cai
 * @date 2016/6/15.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChannelAnnotation {

    /**
     * @return 渠道ID，不可重复
     */
    int channelId();

    /**
     * @return 渠道代码，一般为英文
     */
    String channelLabel();

    /**
     * @return 渠道名备注
     */
    String channelName();
}
