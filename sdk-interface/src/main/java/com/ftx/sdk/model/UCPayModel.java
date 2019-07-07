package com.ftx.sdk.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class UCPayModel {
    // 版本号
    @NotEmpty(message = "ver is null")
    private String ver;

    // 签名参数
    @NotEmpty(message = "sign is null")
    private String sign;

    // 支付结果数据
    @NotNull(message = "data is null")
    private UCPayModelData data;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public UCPayModelData getData() {
        return data;
    }

    public void setData(UCPayModelData data) {
        this.data = data;
    }
}
