package com.geoxus.core.common.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.oauth.GXTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class GXHttpContextUtils {
    @GXFieldCommentAnnotation(zhDesc = "日志对象")
    private static final Logger LOG = LoggerFactory.getLogger(GXHttpContextUtils.class);

    private GXHttpContextUtils() {
    }

    /**
     * 获取HttpRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            return requestAttributes.getRequest();
        }
        return null;
    }

    /**
     * 获取Http请求中的URL
     *
     * @return String
     */
    public static String getDomain() {
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = Objects.requireNonNull(request).getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    /**
     * 获取Http头中的Origin
     *
     * @return String
     */
    public static String getOrigin() {
        HttpServletRequest request = getHttpServletRequest();
        return Objects.requireNonNull(request).getHeader("Origin");
    }

    /**
     * 获取Http请求中的数据
     *
     * @param paramName 参数名字
     * @param clazz     结果类型
     * @return T
     */
    public static <T> T getHttpParam(String paramName, Class<T> clazz) {
        Object jsonRequestBody = "{}";
        final HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (null != httpServletRequest) {
            jsonRequestBody = Optional.ofNullable(httpServletRequest.getAttribute("JSON_REQUEST_BODY")).orElse("{}");
        }
        final JSONObject jsonObject = JSONUtil.toBean(jsonRequestBody.toString(), JSONObject.class);
        final T value;
        if (!jsonObject.isEmpty()) {
            value = jsonObject.getByPath(paramName, clazz);
        } else {
            assert httpServletRequest != null;
            String requestParameter = httpServletRequest.getParameter(paramName);
            if (null == requestParameter) {
                final Object requestAttribute = httpServletRequest.getAttribute(paramName);
                if (null != requestAttribute) {
                    requestParameter = requestAttribute.toString();
                }
            }
            value = Convert.convert(clazz, requestParameter);
        }
        if (null == value) {
            return GXCommonUtils.getClassDefaultValue(clazz);
        }
        return value;
    }

    /**
     * 获取Http头中的header
     *
     * @return String
     */
    public static String getHeader(String headerName) {
        HttpServletRequest request = getHttpServletRequest();
        return Objects.requireNonNull(request).getHeader(headerName);
    }

    /**
     * 从token中获取用户ID
     *
     * @param tokenName   Token的名字
     * @param tokenIdName Token中包含的ID名字
     * @return Long
     */
    public static long getUserIdFromToken(String tokenName, String tokenIdName) {
        final String token = ServletUtil.getHeader(Objects.requireNonNull(getHttpServletRequest()), tokenName, CharsetUtil.UTF_8);
        final Map<String, Object> map = GXTokenManager.decodeUserToken(token);
        if (null != map && !map.isEmpty()) {
            return Convert.toLong(map.get(tokenIdName));
        }
        return 0;
    }

    /**
     * 获取客户端IP
     *
     * @return String
     */
    public static String getClientIP(HttpServletRequest httpServletRequest) {
        String ip = "";
        if (null != httpServletRequest) {
            ip = ServletUtil.getClientIP(httpServletRequest);
        }
        return ip;
    }

    /**
     * 获取客户端IP
     *
     * @return String
     */
    public static String getClientIP() {
        String ip = "";
        if (null != GXHttpContextUtils.getHttpServletRequest()) {
            ip = ServletUtil.getClientIP(GXHttpContextUtils.getHttpServletRequest());
        }
        return ip;
    }

    /**
     * 向HttpServletRequest中设置属性值
     *
     * @param attributeName  属性的名字
     * @param attributeValue 属性的值
     * @return HttpServletRequest
     */
    public static HttpServletRequest setHttpServletRequestAttribute(String attributeName, String attributeValue) {
        final HttpServletRequest servletRequest = getHttpServletRequest();
        assert servletRequest != null;
        servletRequest.setAttribute(attributeName, attributeValue);
        return servletRequest;
    }

    /**
     * 获取HttpServletRequest中设置的属性值
     *
     * @param attributeName 属性的名字
     * @return T
     */
    public static <T> T getHttpServletRequestAttribute(String attributeName, Class<T> clazz) {
        final HttpServletRequest servletRequest = getHttpServletRequest();
        assert servletRequest != null;
        final Object attributeValue = servletRequest.getAttribute(attributeName);
        if (null == attributeValue) {
            return GXCommonUtils.getClassDefaultValue(clazz);
        }
        return Convert.convert(clazz, attributeValue);
    }
}