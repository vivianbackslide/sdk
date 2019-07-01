package com.ftx.sdk.utils;


import com.ftx.sdk.entity.type.PlatfromType;

/**
 * Created by zeta.cai on 2017/3/30.
 */
public class PackageUtil {
    public static Integer getPackage(Integer appId, Integer channelId, PlatfromType type){
        if (appId == null || channelId == null)
            return null;

        StringBuffer packageId = new StringBuffer(appId.toString());
        packageId.append(channelId.toString().substring(0,1)+channelId.toString().substring(3));
        packageId.append(type.getType());
        packageId.append(0);

        return Integer.valueOf(packageId.toString());
    }
    public static Integer getAppId(Integer packageId){
        if (packageId == null)
            return null;
        return Integer.valueOf(packageId.toString().substring(0, 3));
    }
    public static Integer getChannelId(Integer packageId){
        if (packageId == null)
            return null;
        StringBuffer idStr = new StringBuffer(packageId.toString());
        idStr.delete(0, 3);
        idStr.delete(idStr.length()-2, idStr.length());
        idStr.insert(1, "00");
        return Integer.valueOf(idStr.toString());
    }

    public static void main(String args[]){
        System.out.println(getChannelId(18814000));
    }
}
