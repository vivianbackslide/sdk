package com.ftx.sdk.controller.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhenbiao.cai
 * @date 2016/12/30.
 */
@RestController
public class AligamesController {

    @Autowired
    private UCPayController ucPayController;

    @RequestMapping(value = "/charge/aligames")
    public String callback(HttpServletRequest request) {
        return ucPayController.callback(request);
    }
}
