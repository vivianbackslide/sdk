package com.ftx.sdk.filter;

import com.ftx.sdk.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 类描述：请求类过滤器
 *
 * @Author xiaojun.yin
 * @Date 2019/6/10 8:32
 * @Version 1.0
 **/
@Slf4j
//@Component
public class PathFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            log.info("请求进入：[" + httpServletRequest.getServletPath() + "]");
            if (!((HttpServletRequest) request).getServletPath().equals("/charge/huawei")) {
                log.info("参数=" + checkOutParam(request));
            }
            String accessToken = request.getParameter("accessToken");
        } catch (Exception e) {
            log.error("转换出错：", e);
        }
        chain.doFilter(request, response);
    }

    private String checkOutParam(ServletRequest request) {
        try {

            return JacksonUtil.ObjectToJsonString(request.getParameterMap());
        } catch (Exception e) {
            return "带非解析参数";
        }
    }

    @Override
    public void destroy() {

    }
}
