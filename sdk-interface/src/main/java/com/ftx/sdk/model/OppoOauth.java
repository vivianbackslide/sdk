package com.ftx.sdk.model;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * oppo 文档提供的源码
 * @author zhenbiao.cai
 * @date 2016/7/1.
 */
public class OppoOauth {

    public static final String OAUTH_CONSUMER_KEY = "oauthConsumerKey";
    public static final String OAUTH_TOKEN = "oauthToken";
    public static final String OAUTH_SIGNATURE_METHOD = "oauthSignatureMethod";
    public static final String OAUTH_SIGNATURE = "oauthSignature";
    public static final String OAUTH_TIMESTAMP = "oauthTimestamp";
    public static final String OAUTH_NONCE = "oauthNonce";
    public static final String OAUTH_VERSION = "oauthVersion";
    public static final String CONST_SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String CONST_OAUTH_VERSION = "1.0";

    public static String generateBaseString(String oauthAppKey, String oauthToken, String oauthTimestamp, String oauthNonce) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(OAUTH_CONSUMER_KEY)
                    .append("=")
                    .append(URLEncoder.encode(oauthAppKey, "UTF-8"))
                    .append("&")
                    .append(OAUTH_TOKEN)
                    .append("=")
                    .append(URLEncoder.encode(oauthToken, "UTF-8"))
                    .append("&")
                    .append(OAUTH_SIGNATURE_METHOD)
                    .append("=")
                    .append(URLEncoder.encode(CONST_SIGNATURE_METHOD, "UTF-8"))
                    .append("&").append(OAUTH_TIMESTAMP)
                    .append("=")
                    .append(URLEncoder.encode(oauthTimestamp, "UTF-8"))
                    .append("&")
                    .append(OAUTH_NONCE)
                    .append("=")
                    .append(URLEncoder.encode(oauthNonce, "UTF-8"))
                    .append("&")
                    .append(OAUTH_VERSION)
                    .append("=")
                    .append(URLEncoder.encode(CONST_OAUTH_VERSION, "UTF-8"))
                    .append("&");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return sb.toString();
    }

    public static String generateSign(String baseStr, String appSecret) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = null;
            String oauthSignatureKey = appSecret + "&";
            spec = new SecretKeySpec(oauthSignatureKey.getBytes(), "HmacSHA1");
            mac.init(spec);
            byteHMAC = mac.doFinal(baseStr.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            return URLEncoder.encode(String.valueOf(base64Encode(byteHMAC)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return baseStr;
    }

    public static char[] base64Encode(byte[] data) {
        final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
        char[] out = new char[((data.length + 2) / 3) * 4];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;

            int val = (0xFF & (int) data[i]);
            val <<= 8;

            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }

            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }

            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return out;
    }
}
