package com.ftx.sdk.common.utils;

import java.math.BigDecimal;

/**
 * 类描述：数字类工具了你
 *
 * @Author xiaojun.yin
 * @Date 2019/5/29 8:23
 * @Version 1.0
 **/
public class NumberUItils {
    /**
     * 功能描述：获取一个规定长度的长整型
     * @Author xiaojun.yin
     * @date
     * @param len
     * @return
     */
    public static BigDecimal getRandomNumber(int len) {
        String str = StringUtils.createRandomNumber(len);
        return new BigDecimal(str);
    }
}
