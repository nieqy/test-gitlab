package com.funshion.activity.common.log;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(2)
@WebFilter(filterName = "interfaceLogFilter", asyncSupported = true, initParams = {
        @WebInitParam(name = "web-path", value = "/jinli")}, urlPatterns = {"/api/jinli/*", "/api/lottery/*", "/api/address/*",
        "/api/address/*", "/api/redpack/*", "/api/activity/*"},
        dispatcherTypes = {DispatcherType.ASYNC, DispatcherType.REQUEST})
public class InterfaceLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if ((!(request instanceof HttpServletRequest)) || (!(response instanceof HttpServletResponse))) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        doFilter(chain, httpRequest, httpResponse);
    }

    private void doFilter(FilterChain chain, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws IOException, ServletException {
        String level = LogConstant.INFO;
        String uri = httpRequest.getRequestURI();
        // 接口名
        String actionName = "";
        if (uri.lastIndexOf("/") > -1) {
            actionName = uri.substring(uri.lastIndexOf("/") + 1);
        }

        // 特殊接口直接返回
        if ("sendCaptcha".equals(actionName) || uri.endsWith("/h5/consume")) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        ResponseWrapper responseWrapper = new ResponseWrapper(httpResponse);
        long startTime = System.currentTimeMillis();

        String requestBody = "";
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            if (isNullJson(JSONObject.toJSONString(httpRequest.getParameterMap()))) {
                // 防止流读取一次后就没有了, 所以需要将流继续写出去
                RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
                requestBody = HttpHelper.getBodyString(requestWrapper);
                chain.doFilter(requestWrapper, responseWrapper);
            } else {
                // application/x-www-form-urlencoded POST
                requestBody = JSONObject.toJSONString(httpRequest.getParameterMap());
                chain.doFilter(httpRequest, responseWrapper);
            }
        } else {
            requestBody = httpRequest.getQueryString();
            chain.doFilter(httpRequest, responseWrapper);
        }

        // 获取响应消息的retCode和retMsg
        String responseContent = new String(responseWrapper.getDataStream(), "UTF-8");
        httpResponse.getWriter().write(responseContent);

        String result = "";
        if (StringUtils.isNotBlank(responseContent) && responseContent.trim().startsWith("{")) {
            JSONObject jo = JSONObject.parseObject(responseContent);
            StringBuilder sb = new StringBuilder();
            sb.append("retCode=").append(jo.get("retCode")).append(";retMsg=");
            if (jo.get("retMsg") != null) {
                sb.append(jo.get("retMsg"));
            }

            result = sb.toString();

            if ("400".equals(jo.get("retCode"))) {
                level = LogConstant.ERROR;
            }
        } else if ("success".equals(responseContent)) {
            result = "retCode=200;retMsg=ok";
        } else {
            result = responseContent;
        }

        // 记录接口日志
        if (StringUtils.isNotBlank(requestBody) && requestBody.length() > 1000) {
            requestBody = requestBody.substring(0, 1000);
        }
        InterfaceLogManager.log(level, actionName, httpRequest.getRequestURL().toString(), requestBody,
                result, System.currentTimeMillis() - startTime);

    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    private boolean isNullJson(String json) {
        if (StringUtils.isBlank(json) || "{}".equals(json.trim())) {
            return true;
        }
        return false;
    }

}
