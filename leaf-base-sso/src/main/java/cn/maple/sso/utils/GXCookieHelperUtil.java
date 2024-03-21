package cn.maple.sso.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * <p>
 * 注意：在cookie的名或值中不能使用分号（;）、逗号（,）、等号（=）以及空格
 * </p>
 *
 * @author britton birtton@126.com
 * @since 2021-09-16
 */
@Slf4j
public class GXCookieHelperUtil {
    /**
     * 浏览器关闭时自动删除
     */
    public static final int CLEAR_BROWSER_IS_CLOSED = -1;

    /**
     * 立即删除
     */
    public static final int CLEAR_IMMEDIATELY_REMOVE = 0;

    private GXCookieHelperUtil() {
    }

    /**
     * 防止伪造SESSION_ID攻击. 用户登录校验成功销毁当前JSESSIONID. 创建可信的JSESSIONID
     *
     * @param request 当前HTTP请求
     * @param value   用户ID等唯一信息
     */
    public static void authJSESSIONID(HttpServletRequest request, String value) {
        request.getSession().invalidate();
        request.getSession().setAttribute("SSO-" + value, true);
    }

    /**
     * 根据cookieName获取Cookie
     *
     * @param request    请求对象
     * @param cookieName Cookie name
     * @return Cookie
     */
    public static Cookie findCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * 根据 cookieName 清空 Cookie【默认域下】
     *
     * @param response   响应对象
     * @param cookieName cookieName
     */
    public static void clearCookieByName(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
        response.addCookie(cookie);
    }

    /**
     * <p>
     * 清除指定domain的所有Cookie
     * </p>
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param domain   Cookie所在的域
     * @param path     Cookie 路径
     */
    public static void clearAllCookie(HttpServletRequest request, HttpServletResponse response, String domain,
                                      String path) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            clearCookie(response, cookie.getName(), domain, path);
        }
        log.debug("clearAllCookie in  domain " + domain);
    }

    /**
     * <p>
     * 根据cookieName清除指定Cookie
     * </p>
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param cookieName cookie name
     * @param domain     Cookie所在的域
     * @param path       Cookie 路径
     * @return boolean
     */
    public static boolean clearCookieByName(HttpServletRequest request, HttpServletResponse response, String cookieName, String domain, String path) {
        boolean result = false;
        Cookie ck = findCookieByName(request, cookieName);
        if (ck != null) {
            result = clearCookie(response, cookieName, domain, path);
        }
        return result;
    }

    /**
     * 清除指定Cookie 等同于 clearCookieByName(...)
     * 该方法不判断Cookie是否存在,因此不对外暴露防止Cookie不存在异常.
     *
     * @param response   响应对象
     * @param cookieName cookie name
     * @param domain     Cookie所在的域
     * @param path       Cookie 路径
     * @return boolean
     */
    private static boolean clearCookie(HttpServletResponse response, String cookieName, String domain, String path) {
        boolean result = false;
        try {
            Cookie cookie = new Cookie(cookieName, "");
            cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
            if (CharSequenceUtil.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            cookie.setPath(path);
            response.addCookie(cookie);
            log.debug("clear cookie " + cookieName);
            result = true;
        } catch (Exception e) {
            log.error("clear cookie " + cookieName + " is exception!\n" + e);
        }
        return result;
    }

    /**
     * <p>
     * 添加 Cookie
     * </p>
     *
     * @param response 响应对象
     * @param domain   所在域
     * @param path     域名路径
     * @param name     名称
     * @param value    内容
     * @param maxAge   生命周期参数
     * @param httpOnly 只读
     * @param secured  Https协议下安全传输
     */
    public static void addCookie(HttpServletResponse response, String domain, String path, String name, String value, int maxAge, boolean httpOnly, boolean secured) {
        Cookie cookie = new Cookie(name, value);
        // 不设置该参数默认 当前所在域
        if (CharSequenceUtil.isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);

        // Cookie 只在Https协议下传输设置
        if (secured) {
            cookie.setSecure(secured);
        }

        // Cookie 只读设置
        if (httpOnly) {
            addHttpOnlyCookie(response, cookie);
        } else {
            // servlet 3.0 support cookie.setHttpOnly(httpOnly)
            response.addCookie(cookie);
        }
    }

    /**
     * <p>
     * 解决 servlet 3.0 以下版本不支持 HttpOnly
     * </p>
     *
     * @param response HttpServletResponse类型的响应
     * @param cookie   要设置httpOnly的cookie对象
     */
    public static void addHttpOnlyCookie(HttpServletResponse response, Cookie cookie) {
        if (cookie == null) {
            return;
        }
        // 依次取得cookie中的名称、值、 最大生存时间、路径、域和是否为安全协议信息
        String cookieName = cookie.getName();
        String cookieValue = cookie.getValue();
        int maxAge = cookie.getMaxAge();
        String path = cookie.getPath();
        String domain = cookie.getDomain();
        boolean isSecure = cookie.getSecure();
        StringBuilder sf = new StringBuilder();
        sf.append(cookieName).append("=").append(cookieValue).append(";");
        if (maxAge >= 0) {
            sf.append("Max-Age=").append(cookie.getMaxAge()).append(";");
        }
        if (domain != null) {
            sf.append("domain=").append(domain).append(";");
        }
        if (path != null) {
            sf.append("path=").append(path).append(";");
        }
        if (isSecure) {
            sf.append("secure;HTTPOnly;");
        } else {
            sf.append("HTTPOnly;");
        }
        response.addHeader("Set-Cookie", sf.toString());
    }
}
