package com.ftx.sdk.model;

/**
 * Created by jiteng.huang on 2017/6/22.
 */
public class VivoLoginModel {
    private String retcode;
    private VivoLoginData data;

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public VivoLoginData getData() {
        return data;
    }

    public void setData(VivoLoginData data) {
        this.data = data;
    }

    public class VivoLoginData {
        private String success;
        private String openid;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }
}
