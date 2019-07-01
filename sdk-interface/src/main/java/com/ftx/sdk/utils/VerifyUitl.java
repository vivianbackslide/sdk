package com.ftx.sdk.utils;

import com.ftx.sdk.entity.order.CallbackModel;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by zeta.cai on 2017/8/8.
 */
public class VerifyUitl {
    private static Logger logger = LoggerFactory.getLogger(VerifyUitl.class);

    public static boolean verifyPackageId(int packageId) {
        //简单判断packageId有效性
        // TODO: 设计可自校验，不需穿透到库查询有效性的packageId，考虑将最后一位用作校验位
        if (((int) Math.log10(packageId) + 1) != 8) {
            return false;
        }
        return true;
    }

    public static boolean ifTimestampIsMillsecond(Timestamp timestamp) {
        if (null == timestamp) {
            return true; //虽然为null，但是不能说明人家不合格
        }
        return ifTimeIsMillsecond(timestamp.getTime());
    }

    // 形如1534492723000 才是合理时间毫秒数，如果是1534492723，就是秒数喽。
    // 如果是1534492723000000，就太大了，极有可能是转化错了，也不合理
    public static boolean ifTimeIsMillsecond(long time) {
        // 创建时间/当前时间，如果900倍以上，极有可能是1000倍，说明单位不对
        long result = time / System.currentTimeMillis();
        if (result > 900) {
            return false;
        }
        return true;
    }

    public static Boolean verify(String data, String sign, String appSecret) {
        String selfSign = MD5Util.getMD5(data + appSecret);
        return selfSign.equalsIgnoreCase(sign);
    }

    public static Boolean verify(Map<String, String> map, String sign, String appSecret) {
        String data = MapsUtils.createLinkString(map, true, false);
        return verify(data, sign, appSecret);
    }

    public static String createCallbackSign(CallbackModel callback, String secret) {
        Map<String, String> map = MapsUtils.object2StringMap(callback);
        map.remove("sign");
        MapsUtils.removeMapEmptyKey(map);

        return MD5Util.getMD5(MapsUtils.createLinkString(map, true, false) + secret);
    }

    public static Boolean verifyLoginInfo(LoginInfo loginInfo, String appSecret) {
        // 校验签名
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("appId", "" + loginInfo.getAppId());
        requestMap.put("packageId", "" + loginInfo.getPackageId());
        requestMap.put("token", loginInfo.getToken());
        requestMap.put("userId", loginInfo.getUserId());
        requestMap.put("exInfo", loginInfo.getExInfo());
        requestMap.remove("sign");
        MapsUtils.removeMapEmptyKey(requestMap);
        String befSign = MapsUtils.createLinkString(requestMap, true, false) + appSecret;
        String ourSign = MD5Util.getMD5(befSign);

        if (loginInfo.getSign().equalsIgnoreCase(ourSign)) {
            return true;
        } else {
            logger.info("sign校验异常:[our befSign: {}, our sign: {}]", befSign, ourSign);
            return false;
        }
    }

    public static String getSourceIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    public static ArrayList<String> getLocalIps() {
        ArrayList<String> ips = new ArrayList<>();
        try {
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    ips.add(inetAddr.getHostAddress());
                }
            }
        } catch (Exception e) {
            logger.error("getLocalIps error, msg = {}", e.getMessage(), e);
        }
        return ips;
    }
}
