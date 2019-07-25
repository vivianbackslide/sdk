package com.ftx.sdk.model;

public enum TencentInterfaceType {
    Query("query", 0),
    Pay("pay", 1),
    Refund("refund", 2);

    private String name;
    private int type;

    private TencentInterfaceType(String name, int type){
        this.name = name;
        this.type = type;
    }

    public static String getName(int type){
        for (TencentInterfaceType i : TencentInterfaceType.values()){
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
