package com.ftx.sdk.entity.sdk;

import com.ftx.sdk.utils.security.FtxRSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zeta.cai on 2017/8/11.
 */
public class SecurityRequest implements Serializable {
    Logger logger = LoggerFactory.getLogger(SecurityRequest.class);
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException {
        String decryptData =  FtxRSA.decrypt(data);
        logger.debug("decrypt Request Data: {}", decryptData);
        return decryptData;
    }
}
