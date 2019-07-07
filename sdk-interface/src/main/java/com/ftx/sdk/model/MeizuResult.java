package com.ftx.sdk.model;

public class MeizuResult {

    private String code;
    private String message;
    private String redirect;
    private String value;

    public MeizuResult(String code, String message, String redirect, String value) {
        this.code = code;
        this.message = message;
        this.redirect = redirect;
        this.value = value;
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

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}