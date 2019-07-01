/**
 * Copyright  2013-6-17 第七大道-技术支持部-网站开发组
 * 自主运营平台WEB 下午5:58:33
 * 版本号： v1.0
*/

package com.ftx.sdk.common.utils;


import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  @Description:反射工具类
 * @Author: xiaojun.yin
 */
public class ReflectUtil {

	/**
	 * 
	 * <li>创建时间： 2013-6-17 下午6:01:41</li>
	 * <li>创建人：amos.zhou </li>
	 * <li>方法描述 : 寻找方法名唯 一的方法</li>
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method findUniqueMethod(Class<?> clazz, String name) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName())) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	/**
	 * 将map中的值设置到对象属性上
	 * 
	 * @param obj
	 * @param valMap
	 */
	public static void SetFieldValue(Object obj, Map<String, String> valMap) throws Exception{
		setFieldValue(obj, valMap, null);
	}
	
	/**
	 * 将map中的值设置到对象属性上
	 * 
	 * @param obj
	 * @param valMap
	 * @param dateFomart 日期对象格式化格式
	 */
	public static void setFieldValue(Object obj, Map<String, String> valMap, String dateFomart) throws Exception{
		Class<?> clazz = obj.getClass();
		//取出obj里的所有方法
		Method[] methods = clazz.getDeclaredMethods();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String fieldSetName = parSetName(field.getName());
			if (!checkSetMet(methods, fieldSetName)) {
				continue;
			}
			Method fieldSetMet = clazz.getMethod(fieldSetName, field.getType());
			// String fieldKeyName = parKeyName(field.getName());
			String fieldKeyName = field.getName();
			String value = valMap.get(fieldKeyName);
			if (StringUtils.isNullOrEmpty(value)) {
				continue;
			}
			String fieldType = field.getType().getSimpleName();
			if ("String".equals(fieldType)) {
				fieldSetMet.invoke(obj, value);
			} else if ("Date".equals(fieldType)) {
				Date temp = StringUtils.isNullOrEmpty(dateFomart) ? DateUtils.parse(value) : DateUtils.parse(value, dateFomart);
				fieldSetMet.invoke(obj, temp);
			} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
				Integer intval = Integer.parseInt(value);
				fieldSetMet.invoke(obj, intval);
			} else if ("Long".equalsIgnoreCase(fieldType)) {
				Long temp = Long.parseLong(value);
				fieldSetMet.invoke(obj, temp);
			} else if ("Double".equalsIgnoreCase(fieldType)) {
				Double temp = Double.parseDouble(value);
				fieldSetMet.invoke(obj, temp);
			} else if ("Boolean".equalsIgnoreCase(fieldType)) {
				Boolean temp = Boolean.parseBoolean(value);
				fieldSetMet.invoke(obj, temp);
			} else if ("Byte".equalsIgnoreCase(fieldType)) {
				Byte temp = Byte.parseByte(value);
				fieldSetMet.invoke(obj, temp);
			} else if ("Float".equalsIgnoreCase(fieldType)){
				Float temp = Float.parseFloat(value);
				fieldSetMet.invoke(obj, temp);
			} else if ("Short".equalsIgnoreCase(fieldType)){
				Short temp = Short.parseShort(value);
				fieldSetMet.invoke(obj, temp);
			} else if("BigInteger".equalsIgnoreCase(fieldType)){
				BigInteger temp = new BigInteger(value);
				fieldSetMet.invoke(obj, temp);
			} else if("BigDecimal".equalsIgnoreCase(fieldType)){
				BigDecimal temp = new BigDecimal(value);
				fieldSetMet.invoke(obj, temp);
			} else {
				throw new Exception("not support type" + fieldType);
			}
		}
	}
  
  
    /** 
     * 判断是否存在某属性的 set方法 
     *  
     * @param methods 
     * @param fieldSetMet 
     * @return boolean 
     */  
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {  
        for (Method met : methods) {  
            if (fieldSetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 判断是否存在某属性的 get方法 
     *  
     * @param methods 
     * @param fieldGetMet 
     * @return boolean 
     */  
    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {  
        for (Method met : methods) {  
            if (fieldGetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 拼接某属性的 get方法 
     *  
     * @param fieldName 
     * @return String 
     */  
    public static String parGetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_') {
			startIndex = 1;
		}
        return "get"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }  
  
    /** 
     * 拼接在某属性的 set方法 
     *  
     * @param fieldName 
     * @return String 
     */  
    public static String parSetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_') {
			startIndex = 1;
		}
        return "set"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }  
    public static boolean isEmptyObject(Object obj)  
    {  
        if (obj == null){  
            return true;  
        }  
        if ((obj instanceof List)){ 
            return ((List) obj).size() < 1;  
        }  
        if ((obj instanceof String)){  
            return ((String) obj).trim().equals("");  
        } 
        if ((obj instanceof Map)){
            return ((Map) obj).size()<1;  
        } 
        
        return false;  
    }

	/**
	 *  @Description:从object1拷贝值到obj2，非空字段拷贝
	 * @Author: xiaojun.yin
	 * @param from
	 * @param to
	 */
	public static void copyValueNotNull(Object from, Object to) {
		copyValueAllByName(from,to,true);
	}

	/**
	 *  @Description:从object1拷贝值到obj2，全部拷贝
	 * @Author: xiaojun.yin
	 * @param from
	 * @param to
	 */
	public static void copyValueByName(Object from, Object to) {
		copyValueAllByName(from,to,false);
	}
	/**
	 *  @Description:拷贝相同属性
	 * @Author: xiaojun.yin
	 * @param from
	 * @param to
	 */
	private static void copyValueAllByName(Object from, Object to,boolean checkNull) {
		if (from == null || to == null) {
			return;
		}
		try {
			Map<String, Field> srcFieldMap= getAssignableFieldsMap(from);
			Map<String, Field> targetFieldMap = getAssignableFieldsMap(to);
			for (String srcFieldName : srcFieldMap.keySet()) {
				Field srcField = srcFieldMap.get(srcFieldName);
				if (srcField == null) {
					continue;
				}
				// 变量名需要相同
				if (!targetFieldMap.keySet().contains(srcFieldName)) {
					continue;
				}
				Field targetField = targetFieldMap.get(srcFieldName);
				if (targetField == null) {
					continue;
				}
				// 类型需要相同
				if (!srcField.getType().equals(targetField.getType())) {
					continue;
				}
				if (checkNull) {
					Object obj = srcField.get(from);
					if (obj == null) {
						continue;
					}
				}
				targetField.set(to,srcField.get(from));

			}
		}catch (Exception e) {
			// 异常
		}
		return ;
	}

	private static Map<String, Field> getAssignableFieldsMap(Object obj) {
		if (obj == null) {
			return new HashMap<String, Field>();
		}
		Map<String, Field> fieldMap = addALlField(obj.getClass());
		Class clazz = obj.getClass().getSuperclass();
		if (clazz != null) {
			fieldMap.putAll(addALlField(clazz));
		}
		return fieldMap;
	}

	private static Map<String, Field> addALlField(Class clazz) {
		Map<String, Field> fieldMap = new HashMap<>();
		for (Field field : clazz.getDeclaredFields()) {
			// 过滤不需要拷贝的属性
			if (Modifier.isStatic(field.getModifiers())
					|| Modifier.isFinal(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			fieldMap.put(field.getName(), field);
		}
		return fieldMap;
	}


}
