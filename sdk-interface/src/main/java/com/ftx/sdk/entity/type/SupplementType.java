package com.ftx.sdk.entity.type;

/**
 * Created by author.chai on 2018/9/20.
 */
public enum SupplementType{
    Failure_Resend("Failure_Resend", 1),//    我们通知失败的，重新通知（可能是游戏bug，也可能是我们未被加入白名单，后来好了）
    Success_Resend("Success_Resend", 2),//    我们通知成功的，再次通知（游戏不得已，需要我们帮忙）
    ChannelBug("ChannelBug", 3);//    渠道要我们补（渠道不得已，需要我们帮忙）

    private String name;
    private int type;

    SupplementType(String name, int type) {
        this.name = name;
        this.type = type;
    }


    public static SupplementType get(int type) {
        for (SupplementType i : SupplementType.values()) {
            if (i.getType() == type) return i;
        }
        return null;
    }

    public static SupplementType getByName(String name) {
        for (SupplementType i : SupplementType.values()) {
            if (i.getName().equals(name))
                return i;
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
