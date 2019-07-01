package com.ftx.sdk.common.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author xiaojun
 * @date 2013-5-26
 * DES对称加密工具类
 */
public class DESUtil {

    private static Key key;

    /**
     * 根据参数生成KEY
     */
    public static void setKey(String strKey) {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("DES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            _generator.init(secureRandom);
            key = _generator.generateKey();
            _generator = null;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        }
    }

    /**
     * 加密String明文输入,String密文输出
     */
    public static String getEncString(String strMing) {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        Base64.Encoder base64en = Base64.getEncoder();
        try {
            byteMing = strMing.getBytes("UTF8");
            byteMi = getEncCode(byteMing);

            strMi = base64en.encodeToString(byteMi);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            base64en = null;
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    /**
     * 解密 以String密文输入,String明文输出
     * @param strMi
     * @return
     */
    public static String getDesString(String strMi) {
        Base64.Decoder base64De = Base64.getDecoder();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try {
            byteMi = base64De.decode(strMi);
            byteMing = getDesCode(byteMi);
            strMing = new String(byteMing, "UTF8");
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            base64De = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }

    /**
     * 加密以byte[]明文输入,byte[]密文输出
     * @param byteS
     * @return
     */
    private static byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     * @param byteD
     * @return
     */
    private static byte[] getDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    public static void main(String args[]) {
        DESUtil.setKey("the.system.create.by.damogufeng");
        String str1 = "root";
        String str2 = "junge";
//        String str1 = "linshi";
//        String str2 = "linShimyp0ass!2#";
        String str3 = "tWbM8dRbCN8mms+M/q93qg==";
        String str4 = "jdbc:mysql://10.8.2.74:3306/db_qingke_zxx?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2b8";
        String str5 = "jdbc:mysql://10.8.2.74:3306/db_qingke_zxx_conf?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2b8";
//        //DES解密
//        //DES加密
        System.out.println("用户名:" + DESUtil.getEncString(str1));

        System.out.println("密码:" + DESUtil.getEncString(str2));

//        System.out.println("url1:" + DESUtil.getDesString(str3));
        System.out.println("URL2:" + DESUtil.getEncString(str4));

        System.out.println("URL3:" + DESUtil.getEncString(str5));
    }
}
