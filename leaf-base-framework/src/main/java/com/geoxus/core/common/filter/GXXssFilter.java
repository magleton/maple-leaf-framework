package com.geoxus.core.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 */
public class GXXssFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        GXXssHttpServletRequestWrapper xssRequest = new GXXssHttpServletRequestWrapper((HttpServletRequest) request);
        final String uri = xssRequest.getRequestURI();
        if (uri.contains("contents")
                || uri.contains("/activiti/editor/model")
                || uri.contains("save-model.html")
                || uri.contains("/cftf-loan-products/backend/create")
                || uri.contains("/content/backend/create")) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(xssRequest, response);
        }
    }

    @Override
    public void destroy() {
    }
}