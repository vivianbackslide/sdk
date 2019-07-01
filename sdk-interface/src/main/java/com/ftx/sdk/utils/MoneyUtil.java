package com.ftx.sdk.utils;

/**
 * @author zhenbiao.cai
 * @date 2016/6/27.
 */
public class MoneyUtil {

    public static int yuan2fen(String src) {
        return (Integer) (int) (Float.parseFloat(src) * 100);
    }

    public static int yuan2fen(int src) {
        return src * 100;
    }

    public static int yuan2fen(float src) {
        return (Integer) (int)(src * 100);
    }

    public static int yuan2fen(double src) {
        return (Integer) (int)(src * 100);
    }

}
