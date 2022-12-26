package cn.maple.sso.util;

import cn.maple.sso.properties.GXSSOProperties;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <p>
 * HTTP工具类
 * </p>
 *
 * @author britton birtton@126.com
 * @since 2021-09-16
 */
@Slf4j
public class GXHttpUtil {
    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    public static final String X_REQUESTED_WITH = "X-Requested-With";

    /**
     * <p>
     * 判断请求是否为 AJAX
     * </p>
     *
     * @param request 当前请求
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    /**
     * <p>
     * AJAX 设置 response 返回状态
     * </p>
     *
     * @param response
     * @param status   HTTP 状态码
     * @param tip
     */
    public static void ajaxStatus(HttpServletResponse response, int status, String tip) {
        try {
            response.setContentType("text/html;charset=" + GXSSOProperties.getSsoEncoding());
            response.setStatus(status);
            PrintWriter out = response.getWriter();
            out.print(tip);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * <p>
     * 获取当前 URL 包含查询条件
     * </p>
     *
     * @param request
     * @param encode  URLEncoder编码格式
     * @return
     * @throws IOException
     */
    public static String getQueryString(HttpServletRequest request, String encode) throws IOException {
        String url = request.getRequestURL().toString();
        StringBuffer sb = new StringBuffer(url);
        String query = request.getQueryString();
        if (query != null && query.length() > 0) {
            sb.append(url.contains("?") ? "&" : "?").append(query);
        }
        return URLEncoder.encode(sb.toString(), encode);
    }

    /**
     * <p>
     * getRequestURL是否包含在URL之内
     * </p>
     *
     * @param request
     * @param url     参数为以';'分割的URL字符串
     * @return
     */
    public static boolean inContainURL(HttpServletRequest request, String url) {
        boolean result = false;
        if (url != null && !"".equals(url.trim())) {
            String[] urlArr = url.split(";");
            StringBuffer reqUrl = new StringBuffer(request.getRequestURL());
            for (int i = 0; i < urlArr.length; i++) {
                if (reqUrl.indexOf(urlArr[i]) > 1) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * URLEncoder 返回地址
     * </p>
     *
     * @param url      跳转地址
     * @param retParam 返回地址参数名
     * @param retUrl   返回地址
     * @return
     */
    public static String encodeRetURL(String url, String retParam, String retUrl) {
        return encodeRetURL(url, retParam, retUrl, null);
    }

    /**
     * <p>
     * URLEncoder 返回地址
     * </p>
     *
     * @param url      跳转地址
     * @param retParam 返回地址参数名
     * @param retUrl   返回地址
     * @param data     携带参数
     * @return
     */
    public static String encodeRetURL(String url, String retParam, String retUrl, Map<String, String> data) {
        if (url == null) {
            return null;
        }

        StringBuffer retStr = new StringBuffer(url);
        retStr.append(url.contains("?") ? "&" : "?");
        retStr.append(retParam);
        retStr.append("=");
        try {
            retStr.append(URLEncoder.encode(retUrl, GXSSOProperties.getSsoEncoding()));
        } catch (UnsupportedEncodingException e) {
            log.error("encodeRetURL error.{} , {}", url, e.getMessage());
        }

        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                retStr.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        return retStr.toString();
    }

    /**
     * <p>
     * URLDecoder 解码地址
     * </p>
     *
     * @param url 解码地址
     * @return
     */
    public static String decodeURL(String url) {
        if (url == null) {
            return null;
        }
        String retUrl = "";

        try {
            retUrl = URLDecoder.decode(url, GXSSOProperties.getSsoEncoding());
        } catch (UnsupportedEncodingException e) {
            log.error("encodeRetURL error.{} ,{}", url, e.getMessage());
        }

        return retUrl;
    }

    /**
     * <p>
     * GET 请求
     * </p>
     *
     * @param request
     * @return boolean
     */
    public static boolean isGet(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod());
    }

    /**
     * <p>
     * POST 请求
     * </p>
     *
     * @param request
     * @return boolean
     */
    public static boolean isPost(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }

    /**
     * <p>
     * 请求重定向至地址 location
     * </p>
     *
     * @param response 请求响应
     * @param location 重定向至地址
     */
    public static void sendRedirect(HttpServletResponse response, String location) {
        try {
            response.sendRedirect(location);
        } catch (IOException e) {
            log.error("sendRedirect location:{} ,{}", location, e.getMessage());
        }
    }

    /**
     * <p>
     * 获取Request Playload 内容
     * </p>
     *
     * @param request
     * @return Request Playload 内容
     */
    public static String requestPlayload(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * <p>
     * 获取当前完整请求地址
     * </p>
     *
     * @param request
     * @return 请求地址
     */
    public static String getRequestUrl(HttpServletRequest request) {
        StringBuffer url = new StringBuffer(request.getScheme());
        // 请求协议 http,https
        url.append("://");
        // 请求服务器
        url.append(request.getHeader("host"));
        // 工程名
        url.append(request.getRequestURI());
        if (request.getQueryString() != null) {
            // 请求参数
            url.append("?").append(request.getQueryString());
        }
        return url.toString();
    }
}
