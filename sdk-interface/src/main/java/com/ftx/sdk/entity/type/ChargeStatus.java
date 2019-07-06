package com.ftx.sdk.entity.type;

/**
 * Created by zeta.cai on 2017/7/26.
 */
public enum ChargeStatus {
    NewOrder("NewOrder", 0),
    PayFailed("PayFail", 1),
    IllegalAmount("IllegalAmount", 2),
    PaySuccessNotifyFailed("PaySuccessNotifyFailed", 3),
    PaySuccessNotifySuccess("PaySuccessNotifySuccess", 4);


    private String name;
    private int type;

    ChargeStatus(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public static String getName(int type) {
        for (ChargeStatus i : ChargeStatus.values()) {
            if (i.getType() == type)
                return i.name;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
