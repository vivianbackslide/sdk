package com.ftx.sdk.utils.tencent;

import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.model.TencentInterfaceType;
import com.ftx.sdk.model.TencentPayModel;
import com.ftx.sdk.utils.MapsUtils;
import com.ftx.sdk.utils.security.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lei.nie on 2016/7/15.
 */
public class TencentUtil {
    //type 0查询1支付2退款
    public static String createUrl(TencentInterfaceType type, SdkParamCache configCache){

        String hostName = configCache.release() ? "host" : "host_debug";

        String host = configCache.channelAPI().get(hostName);
        String url = configCache.channelAPI().get(type.getName());
        return host + url;
    }

    //type 0查询1支付2退款
    public static Map<String, String> createCookie(TencentPayModel payModel, TencentInterfaceType type){
        String[] locs = {"/mpay/get_balance_m", "/mpay/pay_m", "/mpay/cancel_pay_m"};
        Map<String, String> cookie = new HashMap<>();
        try{
            String sessionId = payModel.getIsQQ() == 1 ? "openid" : "hy_gameid";
            String sessionType = payModel.getIsQQ() == 1 ? "kp_actoken" : "wc_actoken";
            cookie.put("session_id", URLEncoder.encode(sessionId, "utf-8"));
            cookie.put("session_type", URLEncoder.encode(sessionType, "utf-8"));
            cookie.put("org_loc", URLEncoder.encode(locs[type.getType()], "utf-8"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return cookie;
    }

    public static Map<String, String> createRequestData(TencentPayModel payModel, TencentInterfaceType type, SdkParamCache configCache){
        Map<String, String> data = new HashMap<>();
        try{
            String appId = configCache.channelConfig().get("appId");
            data.put("openid", payModel.getOpenId());
            data.put("openkey", payModel.getOpenKey());
            data.put("appid", appId);
            data.put("ts", String.valueOf(System.currentTimeMillis()/1000));
            data.put("sig", createSign(payModel, type, configCache));
            data.put("pf", payModel.getPf());
            data.put("pfkey", payModel.getPfKey());
            data.put("zoneid", String.valueOf(payModel.getZoneId()));
            if(type != TencentInterfaceType.Query){
                data.put("amt", String.valueOf(payModel.getAmt()));
                data.put("billno", payModel.getBillno());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public static String getMessageByCode(int code){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "成功");
        map.put(1001, "参数错误");
        map.put(1004, "余额不足");
        map.put(1018, "登录校验错误");
        String msg = map.containsKey(code) ? map.get(code) : "未知错误";
        return msg;
    }

    private static String createSign(TencentPayModel payModel, TencentInterfaceType type, SdkParamCache configCache){
        try{
            String appId = configCache.channelConfig().get("appId");

            String appKeyName = configCache.release() ? "signKey" : "debugSignKey";
            String appKey = configCache.channelConfig().get(appKeyName);

            String url = "/v3/r" + configCache.channelAPI().get(type.getName());
            String encodedUrl = URLEncoder.encode(url, "utf-8");

            Map<String, String> map = new HashMap<>();
            map.put("openid", payModel.getOpenId());
            map.put("openkey", payModel.getOpenKey());
            map.put("appid", appId);
            map.put("ts", String.valueOf(System.currentTimeMillis()/1000));
            map.put("pf", payModel.getPf());
            map.put("pfkey", payModel.getPfKey());
            map.put("zoneid", String.valueOf(payModel.getZoneId()));
            if(type != TencentInterfaceType.Query){
                map.put("amt", String.valueOf(payModel.getAmt()));
                map.put("billno", payModel.getBillno());
            }
            String mapString = MapsUtils.createLinkString(map, true, false);
            String encodedData = URLEncoder.encode(mapString, "utf-8");

            String wholeUrl = String.format("GET&%s&%s", encodedUrl, encodedData);
            String key = appKey + "&";
            byte[] bytes = HmacSHA1Encrypt(wholeUrl, key);
            return Base64.encode(bytes);
        }
        catch(Exception e){
            return "";
        }
    }

    private static byte[] HmacSHA1Encrypt(String text, String key)
    {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            return mac.doFinal(text.getBytes("UTF-8"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new byte[0];
    }
}
