package com.ftx.sdk.common.utils;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description:字符串工具类
 * @Author: xiaojun.yin
 */
public class StringUtils {
    private static int checkCount = 2;

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true-空  false-不为空
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return true;
        }
        if ("".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * IsNumeric 函数      返回 Boolean 值，指出表达式的运算结果是否为数
     *
     * @param obj
     * @return
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        String str = String.valueOf(obj);
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 解码URLDecode
     *
     * @param ecodeStr
     * @param charsetName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String URLDecode(String ecodeStr, String charsetName) throws UnsupportedEncodingException {
        int i = 0;
        while (i < checkCount) {
            ecodeStr = URLDecoder.decode(ecodeStr, charsetName);
            i++;
        }
        return ecodeStr;
    }

    /**
     * 用于转换map的数值类型
     *
     * @param obj
     * @return
     * @author goff.yin
     * @date 2017-4-11 下午5:50:27
     * @version 1.0.0
     */
    public static int ObjectToInt(Object obj) {
        return Integer.valueOf(String.valueOf(obj));
    }


    /**
     * 生成随机的 n位字符串,不重复
     *  最长61位
     * @param len
     * @return
     */
    public static String createRandomStringNotRepeat(int len) {
        String generateSource = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return createRandomStringNotRepeat(len,generateSource);
    }
    /**
     * 生成随机的 n位字符串,不重复
     *  最长generateSource.length() 位
     * @param len
     * @return
     */
    public static String createRandomStringNotRepeat(int len,String generateSource) {
        //字符源，可以根据需要删减
        String rtnStr = "";
        for (int i = 0; i < len && generateSource.length() > 0; i++) {
            //循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(generateSource.charAt((int) Math.floor(Math.random() * generateSource.length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "");
        }
        return rtnStr;
    }

    /**
     * 功能描述：生成随机的 n位字符串
     *
     * @param len
     * @param hex
     * @return
     * @Author xiaojun.yin
     * @date
     */
    public static String createRandomString(int len, String hex) {
        //字符源，可以根据需要删减
        String rtnStr = "";
        for (int i = 0; i < len; i++) {
            //循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(hex.charAt(new Double(Math.random() * hex.length()).intValue()));
            rtnStr += nowStr;
        }
        return rtnStr;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String getUrlQuery(String strURL) {
        if (isNullOrEmpty(strURL)) {
            return null;
        }
        try {
            URL url = new URL(strURL);
            return url.getQuery();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> urlParams(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = getUrlQuery(URL);
        if (isNullOrEmpty(strUrlParam)) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (isNullOrEmpty(arrSplitEqual[0])) {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * @param len
     * @return
     * @author goff.yin
     * @date 2017-9-19 下午4:15:52
     * @version 1.0.0
     */
    public static String getRandom(int len) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        String hex = "0123456789";
        for (int index = 0; index < len; index++) {
            builder.append(hex.charAt(random.nextInt(10)));
        }
        return builder.toString();
    }

    //字符串转换为ascii
    public static String StringToAscii(String content) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            int c = content.charAt(i);
            result = result.append(c);
        }
        return result.toString();
    }

    //字符串转换为十六进制ascii
    public static String StringToHexAscii(String content) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            int c = content.charAt(i);
            result = result.append(Integer.toHexString(c));
        }
        return result.toString();
    }

    /**
     * 手机号是否合法
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (!isNullOrEmpty(mobile) && mobile.matches("^((13[0-9])|14[57]|17[0-9]|(15[0-3,5-9])|(18[0-9]))\\d{8}$")) {
            return true;
        }
        return false;
    }

    /**
     * 去掉前后空格
     *
     * @param str
     * @return
     */
    public static String trimToEmpty(String str) {
        if (str != null) {
            return str.trim();
        }
        return str;
    }

    /**
     * 检查是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return isNullOrEmpty(str);
    }

    /**
     * @param qingkeList
     * @return
     * @Description:创建随机青稞号
     * @Author: xiaojun.yin
     */
    public static String createQingKeStr(List<String> qingkeList) {
        Map<String, String> qingkeMap = new HashMap<>();
        for (String s : qingkeList) {
            qingkeMap.put(s, s);
        }
        String qingke = getRandom(10);
        while (qingke.startsWith("0") && qingkeMap.containsKey(qingke)) {
            qingke = getRandom(10);
        }
        return qingke;
    }

    public static String createRandomNumber(int len) {
        String generateSource = "0123456789";
        return createRandomString(len, generateSource);
    }

    public static void main(String[] args) {
        String randomNumber = StringUtils.createRandomNumber(32);
        System.out.println(randomNumber);
        for (int i = 40; i < 88; i++) {
//            System.out.println(i+"==>"+StringUtils.createRandomNumber(i));
            BigDecimal b = new BigDecimal(StringUtils.createRandomNumber(i));
            System.out.println(b.toBigInteger().toString());
        }

    }
}
