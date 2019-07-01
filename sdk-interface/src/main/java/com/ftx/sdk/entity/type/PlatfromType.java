package com.ftx.sdk.entity.type;

/**
 * Created by zeta.cai on 2017/3/30.
 */
public enum PlatfromType {
    Android("Android", 0),
    IOS("IOS", 1);

    private String name;
    private int type;

    private PlatfromType(String name, int type){
        this.name = name;
        this.type = type;
    }

    public static String getName(int type){
        for (PlatfromType i : PlatfromType.values()){
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
