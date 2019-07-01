package com.ftx.sdk.exception;

/**
 * Created by zeta.cai on 2017/7/19.
 */
public class ChannelLoginException extends ServerException {

    private static final long serialVersionUID = -9222350184544428510L;

    public ChannelLoginException() {
        super();
    }

    public ChannelLoginException(String message) {
        super(message);
    }
}
