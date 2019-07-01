package com.ftx.sdk.service.config;

import java.util.Map;

/**
 * 描述：缓存变量工具接口类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-06 17:25
 */
public interface SystemConfigOnDBService {

    /**
     * 初始化系统配置
     *
     * @throws Exception
     */
    public void initSystemConfig() throws Exception;

    /**
     * 更新配置
     *
     * @param key
     * @param value
     */
    public void updateConfig(String key, String value, int state) throws Exception;

    /**
     * 根据Key值获取Value
     *
     * @param key
     * @return
     * @throws Exception
     */
    public String getValue(String key) throws Exception;

    public Map<String, String> getAllConfig() throws Exception;

    /**
     * 设置内存中配置值
     *
     * @param key
     * @param value
     * @throws Exception
     */
    public void setValue(String key, String value) throws Exception;

    /**
     * 重新加载系统配置
     */
    public void reloadSysConfig();

    /**
     * 修改系统配置
     */
    public void modifySysConfig();

}
