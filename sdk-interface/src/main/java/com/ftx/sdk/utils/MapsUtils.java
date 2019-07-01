package com.ftx.sdk.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author zhenbiao.cai
 * @date 2016/6/23.
 */
public class MapsUtils {

    /**
     * 把值为 Object 的 Map 转成 String，只适合简单类
     */
    public static Map<String, String> object2StringMap(Object o) {
        Map<String, String> map = Maps.newHashMap();
        Class<?> clazz = o.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(o);
                if (null != value) {
                    map.put(name, String.valueOf(value));
                }
            } catch (IllegalAccessException e) {
            }
        }
        return map;
    }

    /**
     * 移除 Map 中值为 null 或空的 key
     *
     * @param param
     */
    public static void removeMapEmptyKey(Map<String, String> param) {
        List<String> keys = new ArrayList<>(param.keySet());
        for (String key : keys) {
            if (Strings.isNullOrEmpty(param.get(key))) {
                param.remove(key);
            }
        }
    }

    //按照key="value"的模式用'&'字符拼接成字符串,sort 是否需要根据key值作升序排列

    /**
     * 按key值排列，以key=value&key=value的形式拼成一个串
     *
     * @param param
     * @param sort            键是否排序
     * @param isWithQuotation 值是否需要加双引号
     * @return
     */
    public static String createLinkString(Map<String, String> param, boolean sort, boolean isWithQuotation) {
        return createLinkString(param, sort, isWithQuotation, false, null);
    }

    public static String createLinkString(Map<String, String> param, boolean sort, boolean isWithQuotation, boolean encode, Set<String> exclude){
        try {
            List<String> keys = new ArrayList<String>(param.keySet());
            if (sort) {
                Collections.sort(keys);
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                if (exclude != null && exclude.contains(key))
                    continue;

                String value = "";

                if (null != param.get(key)) {
                    value = param.get(key);
                }

                if (encode){
                    key = URLEncoder.encode(key, "utf-8");
                    value = URLEncoder.encode(value, "utf-8");
                }

                if (isWithQuotation) {
                    sb.append(String.format("%s=\"%s\"", key, value));
                } else {
                    sb.append(String.format("%s=%s", key, value));
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException impossible) {
            throw new AssertionError(impossible);
        }
    }

    public static Map<String, String> string2map(String string) {
        if (Strings.isNullOrEmpty(string)) {
            return null;
        }
        Map<String, String> retMap = Maps.newHashMap();
        for (String item : string.split("&")) {
            int index = item.indexOf('=');
            retMap.put(item.substring(0, index), item.substring(index + 1));
        }
        return 0 == retMap.size() ? null : retMap;
    }
}
