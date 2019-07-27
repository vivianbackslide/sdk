package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelAnnotation(channelId = 10001, channelLabel = "sample", channelName = "模拟渠道")
public class SampleLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(SampleLoginHandler.class);

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) {

        //获得该包关于渠道的配置
        String channelAppId = configCache.channelConfig().get("channelAppId");
        String channelAppKey = configCache.channelConfig().get("channelAppKey");
        String channelAppSecret = configCache.channelConfig().get("channelAppSecret");
        logger.debug(String.format("%s %s %s", channelAppId, channelAppKey, channelAppSecret));

        //客户端上传的用户信息在 loginInfo
//		loginInfo.getToken();  //这个必须不为空
//		loginInfo.getUserId(); //这个有可能为空
//		loginInfo.getExInfo(); //这个是额外的信息

        String sampleUserId = loginInfo.getUserId();
        String userId;
        if ("test_123456".equals(sampleUserId) && "token_123456_特殊字符测试+ /?%#&=".equals(loginInfo.getToken())) {
            userId = "test_123";
        } else if ("test_654321".equals(sampleUserId) && "token_654321".equals(loginInfo.getToken())) {
            userId = "test_456";
        } else if (TextUtils.isEmpty(sampleUserId) && "token_123".equals(loginInfo.getToken())) {
            userId = "test_321";
        } else {
            return null;
        }

        User user = new User();
        user.setUserId(userId);
        user.setChannelId(configCache.getChannelId());
        user.setChannelName(configCache.getChannelLabel());
        return user;
    }
}
