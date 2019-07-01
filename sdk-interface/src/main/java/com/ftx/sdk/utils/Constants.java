package com.ftx.sdk.utils;

/**
 * Created by mitnick.cheng on 2016/9/6.
 */
public class Constants {
    public final static long TEN_SECOND = 10 * 1000;

    public final static long ONE_MINUTE = 60 * 1000;
    public final static long TWO_MINUTE = 2 * ONE_MINUTE;
    public final static long THREE_MINUTE = 3 * ONE_MINUTE;

    public final static long ONE_HOUR = 60 * ONE_MINUTE;

    public final static long ONE_DAY = 24 * 60 * ONE_MINUTE;

    public final static long ONE_WEEK = 7 * ONE_DAY;

    public static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    public static final Integer MD5StrLength = 32;
}
