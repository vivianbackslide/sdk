
package com.ftx.sdk.utils.huawei;

import java.util.Map;

public abstract class CommonUtil {

    public static boolean rsaDoCheck(Map<String, Object> params, String sign, String publicKey, String signType) {
        //获取待签名字符串
        //String content = RSA.getSignData(params);
        //验签
        return RSA.rsaDoCheck(params, sign, publicKey, signType);
    }

}
