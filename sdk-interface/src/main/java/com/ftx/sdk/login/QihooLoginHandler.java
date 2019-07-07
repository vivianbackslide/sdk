package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Component;

/**
 * Created by lei.nie on 2016/7/11.
 */

@Component
@ChannelAnnotation(channelId = 10011, channelLabel = "360", channelName = "360")
public class QihooLoginHandler extends LoginHandlerAdapter{
    @Autowired
    private JsonParser jsonParser;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        String url = getLoginRequestURL(configCache);
        ManagedMap<String, String> data = new ManagedMap<String, String>();
        data.put("access_token", loginInfo.getToken());
        try{
            String response = HttpTools.doGet(url, data);
            JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
            String userId = jsonObject.get("id").getAsString();
            return new User(userId, configCache);
        }
        catch(Exception e){
            return null;
        }
    }
}
