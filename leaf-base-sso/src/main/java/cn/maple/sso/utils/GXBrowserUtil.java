package cn.maple.sso.utils;

import cn.hutool.crypto.SecureUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 验证浏览器基本信息
 * </p>
 *
 * @author britton birtton@126.com
 * @since 2021-09-16
 */
public class GXBrowserUtil {
    private GXBrowserUtil() {
    }

    /**
     * <p>
     * 混淆浏览器版本信息，取 MD5 中间部分字符
     * 获取浏览器客户端信息签名值
     * </p>
     *
     * @param request 请求对象
     * @return 获取浏览器客户端信息签名值
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = SecureUtil.md5(request.getHeader("user-agent"));
        if (null == userAgent) {
            return null;
        }
        return userAgent.substring(3, 8);
    }

    /**
     * <p>
     * 请求浏览器是否合法 (只校验客户端信息不校验domain)
     * </p>
     *
     * @param request   请求对象
     * @param userAgent 浏览器客户端信息
     * @return boolean
     */
    public static boolean isLegalUserAgent(HttpServletRequest request, String userAgent) {
        String ua = getUserAgent(request);
        if (null == ua) {
            return false;
        }
        return ua.equals(userAgent);
    }
}
