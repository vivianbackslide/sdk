package com.ftx.sdk.utils.tencent;

import com.google.gson.JsonObject;

import java.net.URLEncoder;

public class TencentResponseUtil {
    public static String create(int code, String message){
        try{
            message = URLEncoder.encode(message, "utf-8");
        }
        catch(Exception e){
            message = e.getMessage();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code);
        jsonObject.addProperty("msg", message);
        return jsonObject.toString();
    }

    public static String create(int code, String message, String orderId){
        try{
            message = URLEncoder.encode(message, "utf-8");
        }
        catch(Exception e){
            message = e.getMessage();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code);
        jsonObject.addProperty("orderId", orderId);
        jsonObject.addProperty("msg", message);
        return jsonObject.toString();
    }

    public static String create(int code, String message, long balance){
        try{
            message = URLEncoder.encode(message, "utf-8");
        }
        catch(Exception e){
            message = e.getMessage();
        }
        JsonObject result = new JsonObject();
        result.addProperty("code", code);
        result.addProperty("msg", message);
        result.addProperty("balance", balance);
        return result.toString();
    }
}
