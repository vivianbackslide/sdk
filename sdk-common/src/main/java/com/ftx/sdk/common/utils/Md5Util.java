package com.ftx.sdk.common.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：md5工具类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-21 22:29
 */
public class Md5Util {


    static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] digest(final byte[] bytes) {
        return getDigest().digest(bytes);
    }

    public static byte[] digest(final String value) {
        return digest(value.getBytes(Charset.defaultCharset()));
    }

    public static String digestAsHex(final String value) {
        return HexUtils.toHexString(digest(value));
    }

    public static String digestAsHex(final byte[] bytes) {
        return HexUtils.toHexString(digest(bytes));
    }

    public static String password(final String passWord) {
        try {
            return Md5Util.digestAsHex(passWord);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    public static void main(String[] args) {


    }

    public static String newDigestAsHex(final String value) {
        return HexUtils.toHexString(digest(value.getBytes(Charset.forName("UTF-8"))));
    }

}
