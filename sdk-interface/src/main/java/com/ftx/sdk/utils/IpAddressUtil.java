package com.ftx.sdk.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：ip地址工具类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-21 22:39
 */
public class IpAddressUtil {

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
