package com.ftx.sdk.login.xiaomi;

public class XiaoMiResult {

    private int errcode;
    private String errMsg;

    public XiaoMiResult() {
    }

    public XiaoMiResult(int errcode, String errMsg) {
        this.errcode = errcode;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return errcode == 200;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
