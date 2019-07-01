package com.ftx.sdk.entity.sdk.result;


/**
 * @author zhenbiao.cai
 * @date 2016/6/15.
 */
public class JsonResult<T> {
    private int code;
    private String message;
    private T data;

    public JsonResult() {}

    public JsonResult(int code) {
        this.code = code;
    }

    public JsonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public JsonResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public JsonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean success() {
        return code == ErrorCode.Success.SUCCESS.getCode();
    }
}
