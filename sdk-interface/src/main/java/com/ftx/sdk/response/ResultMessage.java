package com.ftx.sdk.response;

import com.google.common.collect.Maps;
import com.ftx.sdk.common.constants.ErrorCode;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 描述：通用返回类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-14 20:20
 */
@Data
public class ResultMessage implements Serializable {
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误描述
     */
    private String message;
    /**
     * 结果集
     */
    private Map<String, Object> data = Maps.newHashMap();
    /**
     * 返回实体
     **/
    private Object returnObj;

    /**
     * 是否成功
     *
     * @return
     */
    public boolean succeed() {
        if (ErrorCode.SUCCESS.equals(code)) {
            return true;
        }
        return false;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Object getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(Object returnObj) {
        this.returnObj = returnObj;
    }

    @Override
    public String toString() {
        return "ResultMessage{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
