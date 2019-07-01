package com.ftx.sdk.common.utils;

import java.util.Date;

/**
 * 描述：令牌工具类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-21 22:16
 */
public class TokenUtil {
    /**
     *  @Description:创建用户token
     * @Author: xiaojun.yin
     * @param userId
     * @param qingkeId
     * @return
     */
    public static String createUserToken(Long userId, String qingkeId) {
        String token = userId + qingkeId + DateUtils.format(new Date());
        token = Md5Util.digestAsHex(AESUtil.getEncodeString(token));
        return token;
    }
}
