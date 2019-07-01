package com.ftx.sdk.utils;

import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;

public class Object2Map {

    /**
     * 将对象的属性名和属性值转换为Map的Key-Value形式，去除空值，需要注意数字类型的0
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> object2Map(Object o) throws IllegalAccessException {
        Map<String, Object> map = Maps.newHashMap();
        Class<?> clazz = o.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields){
            field.setAccessible(true);
            String name = field.getName();
            Object value = field.get(o);
            if (null != value){
                map.put(name, value);
            }
        }
        return map;
    }

    public static void map2object(Map map, Object o) throws IllegalAccessException {
        Class<?> clazz = o.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields){
            field.setAccessible(true);
            String name = field.getName();
            if(map.containsKey(name)) {
                field.set(o, map.get(name));
            }
        }
    }
}
