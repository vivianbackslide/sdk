package com.ftx.sdk.common.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 描述：AES加密工具
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-19 23:52
 */
public class AESUtil {

    private static String AES_KEY = "the.system.create.by.damogufeng";
    private static final String AES_ALG         = "AES";
    private static final String AES_SECURE = "SHA1PRNG";
    private static final String AES_PADDING = "AES/ECB/PKCS5Padding";

    private static  Key key;

    public static void setKey(String strKey) {
        AES_KEY = strKey;
    }
    public static String getEncodeString(String src) {
        try {
            Cipher cipher = getpublicClipher();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(src.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDecodeString(String src) {
        try {
            Cipher cipher = getpublicClipher();
            //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(src));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Cipher getpublicClipher() {
        try {
            //生成KEY
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALG);
            SecureRandom secureRandom = SecureRandom.getInstance(AES_SECURE);
            secureRandom.setSeed(AES_KEY.getBytes());
            keyGenerator.init(128,secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            //key转换
            key = new SecretKeySpec(keyBytes, AES_ALG);
            return Cipher.getInstance(AES_PADDING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        String str1 = "root";
        String str2 = "junge";
//        String str1 = "linshi";
//        String str2 = "linShimyp0ass!2#";
        String str3 = "jdbc:mysql://10.8.2.74:3306/db_qingke_zxx?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2b8";
        String str4 = "jdbc:mysql://10.8.2.74:3306/db_qingke_zxx_conf?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2b8";

        System.out.println("用户名:" + AESUtil.getEncodeString(str1));
        System.out.println("密码:" + AESUtil.getEncodeString(str2));
        str1 = "root";
        str2 = "junge";
        System.out.println("用户名:" + AESUtil.getEncodeString(str1));
        System.out.println("密码:" + AESUtil.getEncodeString(str2));

        System.out.println("url1:" + AESUtil.getEncodeString(str3));
        System.out.println("URL2:" + AESUtil.getEncodeString(str4));

        String str6 = "X5uTu8koPOSLZYtzKqngwg==";
        System.out.println("解："+AESUtil.getDecodeString(str6));
    }
}
