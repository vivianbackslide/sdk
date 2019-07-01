package com.ftx.sdk.annotation;

import java.lang.annotation.*;

/**
 * Created by zeta.cai on 2017/3/8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FTXVersionControl {
    /**
     * 标识版本号
     * @return
     */
    String value();
}
