package com.ftx.sdk.login;

import com.ftx.sdk.annotation.FTXVersionControl;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.exception.ServerException;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhenbiao.cai
 * @date 2016/6/15.
 */
public abstract class LoginHandlerAdapter implements LoginHandler {

    private Logger logger = LoggerFactory.getLogger(LoginHandlerAdapter.class);

    @FTXVersionControl("0.0.0")
    protected abstract User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException;

    @Override
    public final User login(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException, ServerException {
        /*if (Strings.isNullOrEmpty(configCache.getChannelDomain())) {
            logger.error("执行验证失败: [未配置渠道接口域名, channel_id={}, channel_label={}]", configCache.getChannelId(), configCache.getChannelLabel());
            throw new ChannelLoginException("channel config domain null");
        }

        if (!configCache.channelAPI().containsKey("api_login")) {
            logger.error("执行验证失败: [未配置渠道登录验证接口, channel_id={}, channel_label={}}]", configCache.getChannelId(), configCache.getChannelLabel());
            throw new ChannelLoginException("channel config api null");
        }*/

        //获取app渠道版本，执行对应渠道登陆校验方法
        String version = configCache.verson();
        if (version == null) {
            return doLogin(configCache, loginInfo);
        }

        try {

            Method latestVersionMethod = null;
            String latestVersion = null;
            for (Method method : this.getClass().getDeclaredMethods()) {
                Annotation annotation = method.getAnnotation(FTXVersionControl.class);
                if (annotation != null) {
                    FTXVersionControl channelVer = (FTXVersionControl) annotation;
                    if (version.equals(channelVer.value())) {
                        Object result = method.invoke(this, configCache, loginInfo);
                        return (User) result;
                    } else {
                        //记录最高版本
                        if (versionHigher(channelVer.value(), latestVersion)) {
                            latestVersion = channelVer.value();
                            latestVersionMethod = method;
                        }
                    }
                }
            }
            //找不到匹配版本，且存在多个版本，选择最高版本，否则执行默认方法
            if (latestVersionMethod != null) {
                Object result = latestVersionMethod.invoke(this, configCache, loginInfo);
                return (User) result;
            } else {
                return doLogin(configCache, loginInfo);
            }

        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.error("登录验证执行错误:[channel={}], {}", configCache.getChannelId(), e.getMessage(), e);
            throw new ServerException("未找到对应版本登录验证方法");
        }
    }

    protected String getLoginRequestURL(SdkParamCache configCache) {
        String domain = configCache.getChannelDomain().trim();
        String api = configCache.channelAPI().get("api_login").trim();
        while (domain.endsWith("/")) {
            domain = domain.substring(0, domain.length() - 1);
        }
        while (api.startsWith("/")) {
            api = api.substring(1, api.length());
        }
        api = Strings.isNullOrEmpty(api) ? "" : ("/" + api);
        return (domain + api).replaceAll(" ", "");
    }

    protected String appendURI(String url, String ... strings){
        while (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        for (String uri : strings){
            while (uri.startsWith("/")) {
                uri = uri.substring(1, uri.length());
            }
            uri = Strings.isNullOrEmpty(uri) ? "" : ("/" + uri);
            url += uri;
        }

        return url;
    }

    private static Boolean versionHigher(String version, String compareVersion) {
        if (compareVersion == null)
            return true;

        String[] slices = version.split("\\.");
        String[] compareSlices = compareVersion.split("\\.");
        for (int i = 0; i < slices.length; ++i) {
            int node = Integer.parseInt(slices[i]);
            int compareNode = Integer.parseInt(compareSlices[i]);

            if (node < compareNode)
                return false;
        }

        return true;
    }
}
