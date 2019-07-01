package com.ftx.sdk.common.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：json转换工具类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-21 11:33
 */
public class JacksonUtil {
    private static ObjectMapper objectMapper;

    static{
        objectMapper = new ObjectMapper();
        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        // 忽略空Bean转Json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 所有日期格式统一为 yyyy:MM:dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT));
        // 忽略在Json字符串中存在，但在Java对象中不存在的对应属性的状况，防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    /**
     * 方法功能描述 :
     * @author xiaojun.yin
     * 2018年11月8日 上午11:42:40
     * @param t
     * @return
     */
    public static <T> String ObjectToJsonString(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            System.out.println("数据转换出错："+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法功能描述 :接送中读取list
     * @author xiaojun.yin
     * 2018年11月8日 上午11:47:37
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> readListValue(String json, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return objectMapper.readValue(json, javaType);
        } catch (JsonParseException e) {
            System.out.println("数据转换出错：json数据格式不正确--》"+json);
            e.printStackTrace();
        } catch (JsonMappingException e) {
            System.out.println("数据转换出错：转成mapping失败--》"+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("数据转换出错：IO异常--》"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法功能描述 :json中读取obj
     * @author xiaojun.yin
     * 2018年11月8日 上午11:54:46
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T readObjectValue(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonParseException e) {
            System.out.println("数据转换出错：json数据格式不正确--》"+json);
            e.printStackTrace();
        } catch (JsonMappingException e) {
            System.out.println("数据转换出错：转成mapping失败--》"+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("数据转换出错：IO异常--》"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 方法功能描述 :对象转成map
     * @author xiaojun.yin
     * 2018年11月8日 下午1:28:12
     * @param t
     * @return
     * @throws IllegalAccessException
     */
    @SuppressWarnings("rawtypes")
    public static <T> Map<String, Object> objectToMap(T t) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class clazz = t.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(t);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 方法功能描述 :map转Object
     * @author xiaojun.yin
     * 2018年11月8日 下午1:30:54
     * @param objMap
     * @param clazz
     * @return
     */
    public static <T> T MapToObject(Map<String, Object> objMap, Class<T> clazz) {
        String objJson = ObjectToJsonString(objMap);
        return readObjectValue(objJson, clazz);
    }


    public  static <T> List<T> readvalue(String fieldName, String zhongJiStr, Class<T> clazz) {
        try {
            JsonNode jn1 = objectMapper.readTree(zhongJiStr);
            List<JsonNode> jns = jn1.findValues(fieldName);
            if(jns.isEmpty()){
                return null;
            }
            return readListValue(JacksonUtil.ObjectToJsonString(jns.get(0)), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
