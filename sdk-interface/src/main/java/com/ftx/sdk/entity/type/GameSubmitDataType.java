package com.ftx.sdk.entity.type;

/**
 * Created by zeta.cai on 2017/6/15.
 */
public enum GameSubmitDataType {
    ROLE_LOGIN(1, "进入游戏"),
    CREATE_ROLE(2, "创建角色"),
    ROLE_LEVEL_UP(3, "角色升级"),
    ROLE_LOGOUT(4, "退出游戏");

    private String name;
    private int type;

    private GameSubmitDataType(int type, String name){
        this.name = name;
        this.type = type;
    }

    public static String getName(int type){
        for (GameSubmitDataType i : GameSubmitDataType.values()){
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
