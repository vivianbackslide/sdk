package com.ftx.sdk.utils.maoer;

import com.google.common.collect.Maps;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by runmin.han on 2018/9/19.
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
                    sb.append(String.format("\"%s\"", value));
                } else {
                    sb.append(String.format("%s", value));
                }
                sb.append("#");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException impossible) {
            throw new AssertionError(impossible);
        }
    }
}
