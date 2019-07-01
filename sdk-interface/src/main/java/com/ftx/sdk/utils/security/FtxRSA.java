package com.ftx.sdk.utils.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.ftx.sdk.utils.security.Coder.decryptBASE64;

/**
 * Created by zeta.cai on 2017/7/18.
 */
public class FtxRSA {

    private static final String serverPublicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8V+/+kIeCrvuPs3m4NFK2ihtnx7LachofGgcOknx+5nSi8VUEuaT3yow5jnpqsrTJ5R8/WRq8EBLq3tjENR03fbz2MYLppcKm5sPhpMgXF+V+yIt25A8j5PidUnlU44RUp+YTX3garh4ueAytC9w73nB+LW5FlIhww8FLnJzkjwIDAQAB";
    private static final String serverPrivateKeyStr = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALxX7/6Qh4Ku+4+zebg0UraKG2fHstpyGh8aBw6SfH7mdKLxVQS5pPfKjDmOemqytMnlHz9ZGrwQEure2MQ1HTd9vPYxgumlwqbmw+GkyBcX5X7Ii3bkDyPk+J1SeVTjhFSn5hNfeBquHi54DK0L3DvecH4tbkWUiHDDwUucnOSPAgMBAAECgYAMNUcBIS0nc2Aq+F8zaXDTFDCikXcuYab/zRgYAbbAIv/8b9gq7j2bMi3UrT+SGvNNAuT7njL1bbVMNBWo4ih6i9F/1RwIVFAWy5AooOPHtXo+H7LE8T8L61fqKPPrI/KNZCHWaXFS/WOYq/xeRQA8ofbLqfiqDQttMlGqlgKVAQJBAOgFfCudEobArsdwxYp/qZrRW1EolRzEZEpSHXE5QNFpjeo3kmFmWoA+xkpYkOCbEceItUjneKLspVg9Orl+gB8CQQDPzuFHO1P3cxPdVrbL6mFcLXkqMejEpBwGaWwPpgn0WlWGSRuNRY0nNVbZB9xyslyUDBX745rxR0pZvxHe802RAkBqnOqbl8dFiBBHsHf01gh3DTAqsL0jxNJYnDbPKgGwuBHvx61JeCCpwS6YRu37ZwXaa+TxJPt30cmALFJpncj9AkB4fGyljkJ64GAOHy7RQE2+0ibETje1e6k8FnevAwgWgyG/QDgXStIyf3yhTSuF3OBMfk1oxGibqEf1jRDhgZPhAkAKhjO1lV6rII7jeSZvVZua/lGGF/z5Wwk+SP5j8lbcltNB2SsKpdweml+tegwKJ22iwyQhh5Z4Y6daiXMKhFUB";
    public static final String clientPublicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZ8EupMBYO6MAyjhohtBYTMCveIUiN0bAq+nmu0bYJuas9JCVhdwis2V2w6DTNOyr4TXxu+7yMAs/mlaNFiEWvUdh1hEvxNo2cef9ylUvzG7JCBvT8A3v6Guhjfo+QXoL7NSsOKqYzM8NG8PTA27KYk8k/ZkU2or7xEPFdRjkRIQIDAQAB";
    public static final String clientPrivateKeyStr = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANnwS6kwFg7owDKOGiG0FhMwK94hSI3RsCr6ea7Rtgm5qz0kJWF3CKzZXbDoNM07KvhNfG77vIwCz+aVo0WIRa9R2HWES/E2jZx5/3KVS/MbskIG9PwDe/oa6GN+j5Begvs1Kw4qpjMzw0bw9MDbspiTyT9mRTaivvEQ8V1GOREhAgMBAAECgYB9/ZPP8FOgNN//mfTzbQeh+iQyGy21hvaAMiMsdGH3h1W+V+ogSU9Dlbm7LtJvnvf9adY41uEuU+XamME1zkzxt1Kj/qmFzH8vTiWT8B7ANLTBrcXcdCSwScjM3B1YeiJdvJcRbhsInIh1siVgAaYAUU+tyrbdjSjq1yM8SzZQbQJBAPbE8P4bOy85OPcdTBQ7D2xN/bzbO21MOoXnpQzEG1Zl4s3UvdH5TEFGAGC7FgKZY/h1ct4ZjNnd/hxcWNkww9sCQQDiF0W63SUZV/+NvhoBgP+N7MEWIth47033vcxyCV+rdq+4ALXJfijZvPw4ME0dnR1XK4+Kia1rdEOEmVGL6g2zAkBN72EVP9BvV6kwEmamtQhn8jHC5ZJdMIgDJmewIndn0AHTc+8HU67LpqoT4sJOy9cQ7zb6AiPD4yKQHnJJFeelAkBl1PGVPrHM/nOeFfeEoeN4uyAkpkE2ByLo6NXCrzIl+mkeyUMXo/kmSRaZADhblD542qfmUCiJ823NtXijOiI9AkB0qp6Xa5jfQqi+iEjIJRtDdtsYhEbxtNtW05/wFiv1Hze800sq6DKFGu8PsV7kihEmNq3+bmNacJ7DyDlW+QLm";

    private static final String KEY_ALGORITHM = "RSA";
    public static final String PADDING_TYPE = "RSA/ECB/PKCS1Padding";
    //RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;
    //RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static Key publicKey;
    private static Key privateKey;

    static {

        try {
            byte[] publicKeyBytes = decryptBASE64(clientPublicKeyStr);
            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory publicKeyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            publicKey = publicKeyFactory.generatePublic(x509KeySpec);

            byte[] privateKeyBytes = decryptBASE64(serverPrivateKeyStr);
            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory privateKeyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            privateKey = privateKeyFactory.generatePrivate(pkcs8KeySpec);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 私钥解密算法
     * @param cryptoSrc 密文
     * @return
     * @throws Exception
     */
    public static String decrypt(String cryptoSrc) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(PADDING_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedData = Base64.decode(cryptoSrc);

        /** 执行解密操作 */
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }

    /**
     * 公钥加密方法
     * @param source 源数据
     * @return
     * @throws Exception
     */
    public static String encrypt(String source) throws Exception {
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(PADDING_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] data = source.getBytes();
        /** 执行分组加密操作 */
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            i++;
            out.write(cache, 0, cache.length);
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();

        return Base64.encode(encryptedData);
    }
}
