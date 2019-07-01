package com.ftx.sdk.utils.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DESCoder {

    public static void main(String[] args){
        String content = "DESCoder";
        // 密码长度必须是8的倍数
        String password = "12345678";
        System.out.println("密　钥：" + password);
        System.out.println("加密前：" + content);
        String result = encrypt(content, password);
        System.out.println("加密后：" + result);
        String decryResult = decrypt(result, password);
        System.out.println("解密后：" + decryResult);
    }

    public static String encrypt(String encryptString, String encryptKey){
//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        IvParameterSpec zeroIv = new IvParameterSpec(encryptKey.getBytes());
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        byte encryptedData[];
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            encryptedData = cipher.doFinal(encryptString.getBytes());
        }catch (Exception ex){
            return "";
        }
        return Base64.encode(encryptedData);
    }
    public static String decrypt(String decryptString, String decryptKey){
        byte[] byteMi = new Base64().decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(decryptKey.getBytes());
//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        byte decryptedData[];
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            decryptedData = cipher.doFinal(byteMi);
        }catch (Exception ex){
            return "";
        }
        return new String(decryptedData);
    }

//    /**
//     * 加密
//     *
//     * @param content
//     *            待加密内容
//     * @param key
//     *            加密的密钥
//     * @return
//     */
//    public static String encrypt(String content, String key) {
//        try {
//            SecureRandom random = new SecureRandom();
//            DESKeySpec desKey = new DESKeySpec(key.getBytes());
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey securekey = keyFactory.generateSecret(desKey);
//            Cipher cipher = Cipher.getInstance("DES");
//            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
//            byte[] result = cipher.doFinal(content.getBytes());
//            return parseByte2HexStr(result);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    /**
//     * 解密
//     *
//     * @param content
//     *            待解密内容
//     * @param key
//     *            解密的密钥
//     * @return
//     */
//    public static String decrypt(String content, String key) {
//        try {
//            byte[] contentByte = parseHexStr2Byte(content);
//            SecureRandom random = new SecureRandom();
//            DESKeySpec desKey = new DESKeySpec(key.getBytes());
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey securekey = keyFactory.generateSecret(desKey);
//            Cipher cipher = Cipher.getInstance("DES");
//            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
//            byte[] result = cipher.doFinal(contentByte);
//            return new String(result);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    /**将二进制转换成16进制
//     * @param buf
//     * @return
//     */
//    public static String parseByte2HexStr(byte buf[]) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < buf.length; i++) {
//            String hex = Integer.toHexString(buf[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            sb.append(hex.toUpperCase());
//        }
//        return sb.toString();
//    }

//    /**将16进制转换为二进制
//     * @param hexStr
//     * @return
//     */
//    public static byte[] parseHexStr2Byte(String hexStr) {
//        if (hexStr.length() < 1)
//            return null;
//        byte[] result = new byte[hexStr.length()/2];
//        for (int i = 0;i< hexStr.length()/2; i++) {
//            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
//            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
//            result[i] = (byte) (high * 16 + low);
//        }
//        return result;
//    }


}

