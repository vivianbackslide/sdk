package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.exception.ServerException;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/6/15.
 */
@Component
public class LoginHandlerManager {

    private Logger logger = LoggerFactory.getLogger(LoginHandlerManager.class);

    private final Map<Integer, LoginHandler> loginHandlerMap = Maps.newHashMap();

    @Autowired
    private ApplicationContext applicationContext;

    public void init() {
        initLoginHandler();
    }

    public void cleanup() {
        loginHandlerMap.clear();
        logger.info("LoginInfo handler dispatcher is destroyed");
    }

    public LoginHandler getLoginHandler(Integer channelId) throws ServerException {
        if(!loginHandlerMap.containsKey(channelId)) {
            logger.error("Login接口异常: [LoginInfo handler not found, channel={}]", channelId);
            throw new ServerException("未找到登录验证处理方法");
        }
        return loginHandlerMap.get(channelId);
    }

    private void initLoginHandler() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("LoadLoginHandlers\n");
            sb.append("===========================================    Init LoginInfo handlers    =============================================").append("\n");
            String[] beanNames = applicationContext.getBeanNamesForAnnotation(ChannelAnnotation.class);
            if (null != beanNames && 0 < beanNames.length) {
                for (String beanName : beanNames) {
                    Object tmpHandler = applicationContext.getBean(beanName);
                    if (LoginHandler.class.isAssignableFrom(tmpHandler.getClass())) {
                        LoginHandler handler = (LoginHandler) tmpHandler;
                        ChannelAnnotation annotation = handler.getClass().getAnnotation(ChannelAnnotation.class);
                        int channelId = annotation.channelId();
                        if (!loginHandlerMap.containsKey(channelId)) {
                            sb.append(String.format("Mapping [ChannelId=%s, ChannelLabel=%s, ChannelName=%s] ==> %s", channelId, annotation.channelLabel(), annotation.channelName(), handler.getClass().getSimpleName())).append("\n");
                            loginHandlerMap.put(channelId, handler);
                        } else {
                            throw new RuntimeException("Init LoginInfo handler error, duplicate handler: ChannelId=" + channelId);
                        }
                    } else {
                        StringBuilder error = new StringBuilder("Init LoginInfo handler error:").append(tmpHandler.getClass());
                        error.append(" annotated ").append(ChannelAnnotation.class.getSimpleName());
                        error.append(" but not implements ").append(LoginHandler.class.getSimpleName());
                        throw new RuntimeException(error.toString());
                    }
                }
                sb.append("=========================================== Init LoginInfo handler finished ===========================================");
            } else {
                sb.append("=================================== Init LoginInfo handler finished, no handler found =================================");
            }
        } catch (Exception e) {
            logger.info("Init LoginInfo handlers error:{}", e.getMessage(), e);
            sb.append("============================================= Init LoginInfo handler error ============================================");
            throw e;
        }finally {
            logger.info(sb.toString());
        }
    }
}
