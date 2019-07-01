package com.ftx.sdk.entity.order;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/6/23.
 */
public class ReNotifierTimeInterval {

    //测试的用这个
//    private final static long first_time_distance = 1 * 60 * 1000;
//    private final static long second_time_distance = 2 * 60 * 1000;
//    private final static long third_time_distance = 3 * 60 * 1000;
//    private final static long forth_time_distance = 4 * 60 * 1000;
//    private final static long fifth_time_distance = 5 * 60 * 1000;
//    private final static long sixth_time_distance = 6 * 60 * 1000;
//    private final static long Seventh_time_distance = 7 * 60 * 1000;
//    private final static long Eighth_time_distance = 8 * 60 * 1000;

    private final static long first_time_distance = 60 * 1000;
    private final static long second_time_distance = 2 * 60 * 1000;
    private final static long third_time_distance = 3 * 60 * 1000;
    private final static long forth_time_distance = 5 * 60 * 1000;
    private final static long fifth_time_distance = 10 * 60 * 1000;
    private final static long sixth_time_distance = 30 * 60 * 1000;
    private final static long Seventh_time_distance = 60 * 60 * 1000;
    private final static long Eighth_time_distance = 150 * 60 * 1000;
    private final static long Ninth_time_distance = 300 * 60 * 1000;
    private final static long Tenth_time_distance = 24 * 60 * 60 * 1000;

    private final static Map<Integer, Long> timeInterval = new HashMap<Integer, Long>() {
        {
            put(1, first_time_distance);
            put(2, second_time_distance);
            put(3, third_time_distance);
            put(4, forth_time_distance);
            put(5, fifth_time_distance);
            put(6, sixth_time_distance);
            put(7, Seventh_time_distance);
            put(8, Eighth_time_distance);
            put(9, Ninth_time_distance);
            put(10, Tenth_time_distance);
        }
    };

    public static Boolean isExistTimeInterval(int n) {
        return !timeInterval.containsKey(n);
    }

    public static Boolean needReNotify(Long beginTime, int notifiedTime)  {
        long futureTime = beginTime + ReNotifierTimeInterval.getTimeInterval(notifiedTime);
        return System.currentTimeMillis() > futureTime;
    }

    public static Long getTimeInterval(int n) {
        return timeInterval.get(n);
    }
}
