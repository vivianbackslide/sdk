package com.ftx.sdk.common.constants;

/**
 * 描述：错误码
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-14 20:04
 */
public class ErrorCode {
    //---------0000 ~ 1000 为基础错误码----------
    /**成功*/
    public static final String SUCCESS = "0000";
    /**失败*/
    public static final String FAILD = "9999";
    /**非法请求*/
    public static final String INVALID_REQUEST = "9995";
    /**参数token异常*/
    public static final String TOKEN_ERROR = "9996";
    /**缺少参数***/
    public static final String LACK_PARAMS = "9997";
    /**错误参数*/
    public static final String ERR_PARAMS = "9998";
    /**缺少权限信息*/
    public static final String LACK_ACCESSTOKEN = "9993";
    /**找不到系统配置*/
    public static final String SYTEM_CONFIG_NOT_EXIST = "9994";
    /**token失效*/
    public static final String ACCESSTOKEN_OVERDUE = "9992";
    /**参数过长*/
    public static final String PARAM_LENGTH_TO_LOOG = "9991";

}
