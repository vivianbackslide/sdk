package com.ftx.sdk.exception;

/**
 * Created by zeta.cai on 2017/7/18.
 */
public class SignVerifyException extends Exception {

    private static final long serialVersionUID = -8891726529942079003L;

    public SignVerifyException(){
        super();
    }

    public SignVerifyException(String msg){
        super(msg);
    }
}
