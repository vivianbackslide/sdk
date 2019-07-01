package com.ftx.sdk.utils.huawei;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.util.Asserts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jiteng.huang on 2018/1/30.
 */
public class SignUtils {
    private static String sign(byte[] data, String privateKey) {
        try {
            byte[] e = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(privateK);
            signature.update(data);
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception var) {
            System.out.println("SignUtil.sign error." + var);
            return "";
        }

    }

    /**
     * 根据参数Map构造排序好的参数串
     *
     * @param params
     * @return
     */
    private static String format(Map<String, String> params) {
        StringBuffer base = new StringBuffer();
        Map<String, String> tempMap = new TreeMap<String, String>(params);
        // 获取计算nsp_key的基础串
        try {
            for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                base.append(k).append("=").append(URLEncoder.encode(v, "UTF-8")).append("&");
            }
        }
        catch (UnsupportedEncodingException e) {
            System.out.println("Encode parameters failed.");
            e.printStackTrace();
        }
        String body = base.toString().substring(0, base.toString().length() - 1);
        // 空格和星号转义
        body = body.replaceAll("\\+", "%20").replaceAll("\\*", "%2A");
        return body;
    }

    public static String generateCPSign(Map<String,String> requestParams,final String cpAuthKey) {
        // 对消息体中查询字符串按字典序排序并且进行URLCode编码
        String baseStr = format(requestParams);
        // 用CP侧签名私钥对上述编码后的请求字符串进行签名
        String cpSign = sign(baseStr.getBytes(Charset.forName("UTF-8")), cpAuthKey);
        return cpSign;
    }

    /**
     * 校验数字签名
     *
     * @param data 加密的数据，格式为：a=x&b=y  example：rtnCode=0&ts=1500552495471
     * @param publicKey RSA 签名公钥(BASE64编码)
     * @param sign 数字签名：游戏服务端生成的RSA签名
     *
     * @return true : 验证成功; false:验证失败
     *
     */
    public static boolean verify(String data, String publicKey, String sign) {
        Asserts.notNull(data, "Encrypt data cant not be null.");
        Asserts.notNull(publicKey, "Public key can not be null.");
        Asserts.notNull(sign, "Sign can not be null.");
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(publicK);
            signature.update(data.getBytes("UTF-8"));
            return signature.verify(Base64.decodeBase64(sign));
        }
        catch (Exception e) {
            System.out.println("SignUtil.verify error." + e);
            return false;
        }
    }
}
