package com.ftx.sdk.entity.sdk.result;


import com.ftx.sdk.utils.security.FtxRSA;

import java.net.URLEncoder;

/**
 * Created by zeta.cai on 2017/8/11.
 */
public class SecurityResponse {
    public static String toSecurity(String data) throws Exception {
        return URLEncoder.encode(FtxRSA.encrypt(data), "utf-8");
    }
}
