package com.ftx.sdk.common.utils;

import java.util.*;

/**
 * 描述：数组校验工具类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-05-13 21:15
 */
public class ArrayListUtil {
    /**
     *  @Description:数组转list并去重
     * @Author: xiaojun.yin
     * @param strArs
     * @return
     */
    public static List<String> ArrayAsListNotReapeat(String[] strArs) {
        Set<String> strSet = new HashSet<>();
        for (String str : strArs) {
            strSet.add(str);
        }
        List<String> resList = new ArrayList<>();
        Iterator<String> it = strSet.iterator();
        while (it.hasNext()) {
            resList.add(it.next());
        }
        return resList;
    }

    /**
     *  @Description:判断list是否为空，
     * @Author: xiaojun.yin
     * @param list
     * @return true 不为空 ，false 为空
     */
    public static boolean checkListIsNotEmpty(List list) {
        return list != null && list.size() > 0;
    }

}
