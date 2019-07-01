package com.ftx.sdk.entity;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zeta.cai on 2017/7/5.
 * <p>
 * 配置参数
 */
public class ConfigBean {
    public static final String SERVER_PORT = "server_port";
    public static final String SERVER_TIMEOUT = "server_timeout";

    private static Map<String, String> configMap;

    static {
        URL configPath = ConfigBean.class.getClassLoader().getResource("application.properties");
        configMap = new HashMap<String, String>();

        try {
            Properties pro = new Properties();
            InputStream is = new FileInputStream(new File(configPath.getPath()));
            pro.load(is);
            Enumeration<?> keys = pro.propertyNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = pro.getProperty(key);
                configMap.put(key, value);
            }
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setConfigMap(Map<String, String> configMap) {
        ConfigBean.configMap = configMap;
    }

    /**
     * 获取值
     */
    public static String getStringValue(String configKey) {
        return ConfigBean.configMap.get(configKey);
    }

    /**
     * 获取值
     */
    public static int getIntValue(String configKey) {
        String s = ConfigBean.configMap.get(configKey);
        return Integer.parseInt(s);
    }


}
