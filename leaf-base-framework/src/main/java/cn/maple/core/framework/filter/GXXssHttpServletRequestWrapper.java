package cn.maple.core.framework.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * XSS过滤处理
 */
public class GXXssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * HTML过滤
     */
    private static final GXHTMLFilter htmlFilter = new GXHTMLFilter();

    /**
     * 没被包装过的HttpServletRequest(特殊场景, 需要自己过滤)
     */
    HttpServletRequest orgRequest;

    public GXXssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    /**
     * 获取最原始的request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof GXXssHttpServletRequestWrapper) {
            return ((GXXssHttpServletRequestWrapper) request).getOrgRequest();
        }
        return request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 非json类型，直接返回
        if (!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(super.getHeader(HttpHeaders.CONTENT_TYPE))) {
            return super.getInputStream();
        }

        // 为空，直接返回
        //String json = IoUtil.read(super.getInputStream(), StandardCharsets.UTF_8);
        String json = StrUtil.str(IoUtil.readBytes(super.getInputStream(), false), StandardCharsets.UTF_8);
        if (CharSequenceUtil.isBlank(json)) {
            return super.getInputStream();
        }

        // xss过滤
        json = xssEncode(json);
        Object jsonRequestBody = Objects.requireNonNull(GXCurrentRequestContextUtils.getHttpServletRequest()).getAttribute("JSON_REQUEST_BODY");
        if (ObjectUtil.isEmpty(jsonRequestBody)) {
            Objects.requireNonNull(GXCurrentRequestContextUtils.getHttpServletRequest()).setAttribute("JSON_REQUEST_BODY", json);
        }
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // 自定义读取的监听器
            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (CharSequenceUtil.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            final String[] values = entry.getValue();
            final String key = entry.getKey();
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (CharSequenceUtil.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    private String xssEncode(String input) {
        return htmlFilter.filter(input);
    }

    /**
     * 获取最原始的request
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }
}
