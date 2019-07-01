package com.ftx.sdk.controller.channel;

import com.ftx.sdk.annotation.ChannelAnnotation;
import com.ftx.sdk.entity.sdk.SdkParamCache;
import com.ftx.sdk.entity.user.LoginInfo;
import com.ftx.sdk.entity.user.User;
import com.ftx.sdk.exception.ChannelLoginException;
import com.ftx.sdk.login.LoginHandlerAdapter;
import com.ftx.sdk.utils.HttpTools;
import com.ftx.sdk.utils.huawei.RSAUtil;
import com.ftx.sdk.utils.huawei.SignUtils;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhenbiao.cai
 * @date 2016/7/1.
 */
@Component
@ChannelAnnotation(channelId = 10008, channelLabel = "huawei", channelName = "华为")
public class HuaweiLoginHandler extends LoginHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(HuaweiLoginHandler.class);
    @Autowired
    private JsonParser jsonParser;
    @Autowired
    private Gson gson;

    @Override
    protected User doLogin(SdkParamCache configCache, LoginInfo loginInfo) throws ChannelLoginException {

        try {
            String exInfo = loginInfo.getExInfo();
            if (!TextUtils.isEmpty(exInfo)) {
                String gameAuthSign = loginInfo.getToken();
                String playerId = loginInfo.getUserId();
                if (TextUtils.isEmpty(playerId)) {
                    String exinfo = loginInfo.getExInfo();
                    JsonObject obj = new JsonParser().parse(exinfo).getAsJsonObject();
                    String userId = obj.get("playerId").getAsString();
                    String ts = obj.get("ts").getAsString();
                    String playerLevel = obj.get("playerLevel").getAsString();

                    String appId = configCache.channelConfig().get("appId");
                    String cpId = configCache.channelConfig().get("cpId");
                    String privateKey = configCache.channelConfig().get("buoyKey");

                    String loginUrl = "https://gss-cn.game.hicloud.com/gameservice/api/gbClientApi";

                    Map<String, String> request = new HashMap<>();
                    request.put("method", "external.hms.gs.checkPlayerSign");
                    request.put("appId", appId);
                    request.put("cpId", cpId);
                    request.put("ts", ts);
                    request.put("playerId", userId);
                    request.put("playerLevel", playerLevel);
                    request.put("playerSSign", gameAuthSign);
                    request.put("cpSign", SignUtils.generateCPSign(request, privateKey));

                    String response = HttpTools.doPost(loginUrl, request);
                    if (Strings.isNullOrEmpty(response)) {
                        logger.error("huawei登录API异常: [第三方接口无返回, app_id={}, loginUrl={}, request={}]", loginInfo.getAppId(), loginUrl, request);
                        return null;
                    }
                    JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
                    int code = jsonObject.get("rtnCode").getAsInt();
                    if (0 == code) {
                        return new User(userId, configCache);
                    } else {
                        logger.error("huawei登录API异常: [登录返回失败状态, app_id={}, response={}]", loginInfo.getAppId(), response);
                        return null;
                    }

                } else {
                    String ts = loginInfo.getExInfo();

                    String appId = configCache.channelConfig().get("appId");
                    String publicKey = configCache.channelConfig().get("loginPublicKey");

                    String sign = appId + ts + playerId;
                    if (!RSAUtil.verify(sign.getBytes("UTF-8"), publicKey, gameAuthSign)) {
                        return null;
                    }

                    return new User(playerId, configCache);
                }

            } else {
                String token = URLEncoder.encode(loginInfo.getToken(), "utf-8");
                token = token.replace("+", "%2B");

                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                String request = String.format("nsp_svc=OpenUP.User.getInfo&nsp_ts=%s&access_token=%s", timestamp, token);

                String requestURL = getLoginRequestURL(configCache);
                logger.info("调用华为登录验证API: [url={}, parameters={}, login_info={}]", requestURL, request, loginInfo.toString());
                String response = HttpTools.doPostByConnection(requestURL, request);
                if (Strings.isNullOrEmpty(response)) {
                    logger.error("huawei登录API异常: [第三方接口无返回, app_id={}, channel_id={}]", loginInfo.getAppId(), configCache.getChannelId());
                    return null;
                }

                JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
                if (jsonObject.has("userID")) {
                    String userId = jsonObject.get("userID").getAsString();
                    if (Strings.isNullOrEmpty(userId)) {
                        throw new ChannelLoginException("user id is null");
                    } else {
                        return new User(userId, configCache);
                    }
                } else {
                    String error = jsonObject.has("error") ? jsonObject.get("error").getAsString() : "unknown error";
                    logger.warn("huawei登录API异常: [第三方接口返回非成功状态, loginInfo={}, result={}]",  gson.toJson(loginInfo), response);
                    throw new ChannelLoginException("接口返回失败状态");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}