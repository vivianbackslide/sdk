package com.ftx.sdk.login;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.security.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhenbiao.cai
 * @date 2016/12/29.
 */
@Component
@ChannelAnnotation(channelId = 10064, channelLabel = "aligames", channelName = "阿里游戏")
public class AligamesLoginHandler extends LoginHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(AligamesLoginHandler.class);

    @Autowired
    private JsonParser jsonParser;
    @Autowired
    private Gson gson;

    public JsonObject genComm(SdkParamCache configCache, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append("sid=").append(token);
        sb.append(configCache.channelConfig().get("channelAppKey"));
        String sign = MD5Util.getMD5(sb.toString());

        JsonObject data = new JsonObject();
        data.addProperty("sid", token);

        JsonObject game = new JsonObject();
        game.addProperty("gameId", configCache.channelConfig().get("channelGameId"));

        JsonObject param = new JsonObject();
        param.addProperty("id", System.currentTimeMillis() / 1000);
        param.add("data", data);
        param.add("game", game);
        param.addProperty("sign", sign);

        return param;
    }

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {
        logger.debug("aligames登录验证: [loginInfo={}]", gson.toJson(loginInfo));
        JsonObject jsonObject = genComm(configCache, loginInfo.getToken());
        String url = getLoginRequestURL(configCache);
        String response = HttpTools.doPostByConnection(url, jsonObject.toString());
        JsonObject repObj = jsonParser.parse(response).getAsJsonObject();
        JsonObject state = repObj.getAsJsonObject("state");

        int code = state.get("code").getAsInt();
        if (1 == code) {
            logger.debug("aligames登录API登录验证成功:[login_info={}, result={}]", loginInfo.toString(), response);
            JsonObject jsonData = repObj.get("data").getAsJsonObject();
            User user = new User();
            user.setChannelId(configCache.getChannelId());
            user.setChannelName(configCache.getChannelLabel());
            user.setUserId(jsonData.get("accountId").getAsString());
            return user;
        } else {
            logger.error("aligames登录API异常: [第三方接口返回非成功状态, logininfo={}, request={}, result={}]", gson.toJson(loginInfo), jsonObject.toString(), response);
            return null;
        }
    }
}
