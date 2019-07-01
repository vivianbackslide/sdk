package com.ftx.sdk.properties;

import com.ftx.sdk.common.utils.AESUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;
import java.util.Set;

/**
 * 描述：解析数据库密码
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-06 15:47
 */
public class PropertyDecipher extends PropertyPlaceholderConfigurer {

    //key过滤数组
    String[] keyFilter = new String[] {
            "master.url", "master.username", "master.password" ,
            "slave.url", "slave.username", "slave.password" ,
            "conf.url", "conf.username", "conf.password",
            "mail.password"
    };
    //DES解密密钥
    private static final String keyStr = "the.system.create.by.damogufeng";

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        AESUtil.setKey(keyStr);
        Set<Object> keySet = props.keySet();
        for(Object str: keySet){
            String keyStr = str.toString();
            for(int i = 0; i < keyFilter.length ; i++ ){
                if(keyStr.indexOf(keyFilter[i]) != -1) {
                    //解密处理
                    String desStr = AESUtil.getDecodeString(props.getProperty(keyStr));
                    props.setProperty(keyStr, desStr);
                }
            }
        }
        //解析非加密properties
        super.processProperties(beanFactory, props);
    }
}
