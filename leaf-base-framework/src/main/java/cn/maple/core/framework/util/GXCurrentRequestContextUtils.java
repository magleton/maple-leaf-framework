package cn.maple.core.framework.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Optional;

public class GXCurrentRequestContextUtils {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(GXCurrentRequestContextUtils.class);

    private GXCurrentRequestContextUtils() {
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
        HttpServletRequest request = Objects.requireNonNull(getHttpServletRequest());
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
        if (Objects.isNull(request)) {
            LOG.info("本次请求是RPC调用,没有HttpServletRequest对象");
            return null;
        }
        return ServletUtil.getHeader(request, headerName, CharsetUtil.UTF_8);
    }

    /**
     * 获取Http头中的header
     *
     * @param headerName  header头的名字
     * @param targetClass 返回的数据类型
     * @return 指定的目标类型
     */
    public static <T> T getHeader(String headerName, Class<T> targetClass) {
        return getHeader(headerName, targetClass, null);
    }

    /**
     * 获取Http头中的header
     *
     * @param headerName   header头的名字
     * @param targetClass  返回的数据类型
     * @param defaultValue 默认值
     * @return 指定的目标类型
     */
    public static <T> T getHeader(String headerName, Class<T> targetClass, T defaultValue) {
        String headerValue = getHeader(headerName);
        if (Objects.isNull(headerValue)) {
            return defaultValue;
        }
        return Convert.convert(targetClass, headerValue);
    }

    /**
     * 从token中获取登录用户的指定字段的值
     * <p>
     * {@code
     * getLoginFieldFromToken("token" , "username" , String.class , "123456");
     * getLoginFieldFromToken("token" , "userId" , Integer.class , "123456");
     * }
     *
     * @param tokenName      header中Token的名字 eg : Authorization、token、adminToken
     * @param tokenFieldName Token中包含的ID名字 eg : id、userId、adminId、username、nickname....
     * @param clazz          返回值类型
     * @param secretKey      加解密KEY
     * @return R
     */
    public static <R> R getLoginFieldFromToken(String tokenName, String tokenFieldName, Class<R> clazz, String secretKey) {
        Object attribute = getHttpServletRequestAttribute(tokenFieldName, clazz);
        if (Objects.nonNull(attribute)) {
            return Convert.convert(clazz, attribute);
        }
        if (Objects.isNull(secretKey)) {
            secretKey = GXTokenConstant.USER_TOKEN_SECRET_KEY;
        }
        Dict dict = getLoginCredentials(tokenName, secretKey);
        Object retValue = dict.getObj(tokenFieldName);
        if (Objects.nonNull(retValue)) {
            return Convert.convert(clazz, retValue);
        }
        return null;
    }

    /**
     * 从token中获取登录信息
     *
     * @param tokenName token名字
     * @param secretKey token密钥
     * @return Dict
     */
    public static Dict getLoginCredentials(String tokenName, String secretKey) {
        // 减少token的二次解密操作 在GXSSOAuthorizationInterceptor拦截器中已经解密过一次并且已经将解密之后的token放入了当前请求对象中
        HttpServletRequest request = getHttpServletRequest();
        assert request != null;
        Object attribute = request.getAttribute(GXCommonConstant.SSO_TOKEN_ATTR);
        if (Objects.nonNull(attribute)) {
            Dict tokenDict = (Dict) attribute;
            if (!tokenDict.isEmpty()) {
                return tokenDict;
            }
        }
        // 从请求对象中获取token并且解密
        String token = getHeader(tokenName);
        if (CharSequenceUtil.isBlank(token)) {
            LOG.error("{}不存在", tokenName);
            return Dict.create();
        }
        // 判断token是否是一个正确的base64字符串
        if (!GXCommonUtils.isBase64(token)) {
            LOG.error("token不是一个有效的base64字符串!");
            return Dict.create();
        }
        String s = GXAuthCodeUtils.authCodeDecode(token, secretKey);
        if (CharSequenceUtil.equalsIgnoreCase("{}", s)) {
            return Dict.create();
        }
        return JSONUtil.toBean(s, Dict.class);
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
        HttpServletRequest httpServletRequest = GXCurrentRequestContextUtils.getHttpServletRequest();
        if (Objects.nonNull(httpServletRequest)) {
            ip = getClientIP(httpServletRequest);
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
        final HttpServletRequest servletRequest = Objects.requireNonNull(getHttpServletRequest());
        final Object attributeValue = servletRequest.getAttribute(attributeName);
        if (null == attributeValue) {
            return null;
        }
        return Convert.convert(clazz, attributeValue);
    }

    /**
     * 判断是否是RPC调用
     * 如果是RPC调用 则不会存在HttpServletRequest对象
     *
     * @return true 是 ; false 不是
     */
    public static boolean isRPC() {
        HttpServletRequest request = getHttpServletRequest();
        return Objects.isNull(request);
    }

    /**
     * 判断是否是HTTP调用
     * 如果是HTTP调用 则会存在HttpServletRequest对象
     *
     * @return true 是 ; false 不是
     */
    public static boolean isHTTP() {
        HttpServletRequest request = getHttpServletRequest();
        return Objects.nonNull(request);
    }

    /**
     * 判断是否是IP　V4内网IP
     *
     * @param ip IP地址
     * @return 是否IPV4内网IP
     */
    public static boolean isInternalIP(byte[] ip) {
        if (ip.length != 4) {
            throw new GXBusinessException("illegal ipv4 bytes", HttpStatus.HTTP_INTERNAL_ERROR);
        }
        //10.0.0.0~10.255.255.255
        //172.16.0.0~172.31.255.255
        //192.168.0.0~192.168.255.255
        if (ip[0] == (byte) 10) {
            return true;
        } else if (ip[0] == (byte) 172) {
            return ip[1] >= (byte) 16 && ip[1] <= (byte) 31;
        } else if (ip[0] == (byte) 192) {
            return ip[1] == (byte) 168;
        }
        return false;
    }

    /**
     * 判断是否是IP　V6内网IP
     *
     * @param inetAddr 网络地址
     * @return 是否IP　V6内网IP
     */
    public static boolean isInternalV6IP(InetAddress inetAddr) {
        // Site local ipv6 address: fec0:xx:xx...
        return inetAddr.isAnyLocalAddress() // Wild card ipv6
                || inetAddr.isLinkLocalAddress() // Single broadcast ipv6 address: fe80:xx:xx...
                || inetAddr.isLoopbackAddress() //Loopback ipv6 address
                || inetAddr.isSiteLocalAddress();
    }
}
