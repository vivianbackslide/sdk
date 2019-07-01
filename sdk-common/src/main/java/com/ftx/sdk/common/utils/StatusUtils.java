package com.ftx.sdk.common.utils;

/**
 * 类描述：状态工具类
 *
 * @Author xiaojun.yin
 * @Date 2019/5/30 20:01
 * @Version 1.0
 **/
public class StatusUtils {
    /**
     * 功能描述：判断是否为点赞
     * @Author xiaojun.yin
     * @date
     * @param isThumbsup
     * @return  true-是 false-否
     */
    public static boolean checkNoticeIsThumbsup(Integer isThumbsup) {
        if (isThumbsup == null) {
            return false;
        }
        if (isThumbsup.intValue() == 1) {
            return true;
        }
        return false;
    }
}
