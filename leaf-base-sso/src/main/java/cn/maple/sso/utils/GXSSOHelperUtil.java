package cn.maple.sso.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXAuthCodeUtils;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.sso.cache.GXSSOCache;
import cn.maple.sso.plugins.GXSSOPlugin;
import cn.maple.sso.properties.GXSSOConfigProperties;
import cn.maple.sso.properties.GXSSOProperties;
import cn.maple.sso.service.GXAbstractSSOService;
import cn.maple.sso.service.GXTokenConfigService;
import cn.maple.sso.service.impl.GXConfigurableAbstractSSOServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * SSO 帮助类
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public class GXSSOHelperUtil {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GXSSOHelperUtil.class);
    /**
     * SSO配置
     */
    protected static GXSSOProperties ssoConfig;
    /**
     * SSO 服务处理
     */
    protected static GXAbstractSSOService ssoService;

    /**
     * 私有构造函数
     */
    private GXSSOHelperUtil() {

    }

    /**
     * 获取GXSsoConfig配置对象
     *
     * @return GXSsoConfig
     * @author britton
     * @since 2021-09-17
     */
    public static GXSSOProperties getSSOConfig() {
        // 为每个应用设置自己的配置信息
        if (Objects.isNull(ssoConfig)) {
            if (Objects.nonNull(GXSpringContextUtils.getBean(GXSSOConfigProperties.class))) {
                ssoConfig = Objects.requireNonNull(GXSpringContextUtils.getBean(GXSSOConfigProperties.class)).getConfig();
            } else {
                ssoConfig = new GXSSOProperties();
            }
            // 为每个应用配置自己的插件
            Map<String, GXSSOPlugin> ssoPluginMap = GXSpringContextUtils.getBeans(GXSSOPlugin.class);
            if (!ssoPluginMap.isEmpty()) {
                ArrayList<GXSSOPlugin> plugins = new ArrayList<>();
                ssoPluginMap.forEach((key, val) -> plugins.add(val));
                GXSSOHelperUtil.getSSOConfig().setPluginList(plugins);
            }
            // 为每个应用配置自己的SsoCache实例
            if (Objects.nonNull(GXSpringContextUtils.getBean(GXSSOCache.class))) {
                GXSSOHelperUtil.getSSOConfig().setCache(GXSpringContextUtils.getBean(GXSSOCache.class));
            }
        }
        return ssoConfig;
    }

    /**
     * 设置GXSsoConfig配置对象
     *
     * @return GXSsoConfig
     * @author britton
     * @since 2021-09-17
     */
    public static GXSSOProperties setSsoConfig(GXSSOProperties ssoConfig) {
        GXSSOHelperUtil.ssoConfig = ssoConfig;
        return GXSSOHelperUtil.ssoConfig;
    }

    /**
     * Sso 服务初始化
     */
    public static GXAbstractSSOService getSSOService() {
        if (Objects.isNull(ssoService)) {
            if (Objects.nonNull(GXSpringContextUtils.getBean(GXAbstractSSOService.class))) {
                ssoService = GXSpringContextUtils.getBean(GXAbstractSSOService.class);
            } else {
                ssoService = new GXConfigurableAbstractSSOServiceImpl();
            }
        }
        return ssoService;
    }

    // ------------------------------- 登录相关方法 -------------------------------

    /**
     * 设置加密 Cookie（登录验证成功）<br>
     * 最后一个参数 true 销毁当前JSESSIONID. 创建可信的 JSESSIONID 防止伪造 SESSIONID 攻击
     * <p>
     * 最后一个参数 false 只设置 cookie
     * <p>
     * request.setAttribute(GXSsoConfig.SSO_COOKIE_MAX_AGE, maxAge);<br>
     * 可以动态设置 Cookie maxAge 超时时间 ，优先于配置文件的设置，无该参数 - 默认读取配置文件数据 。<br>
     * maxAge 定义：-1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param ssoToken   SSO 票据
     * @param invalidate 销毁当前 JSESSIONID
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, Dict ssoToken, boolean invalidate) {
        if (invalidate) {
            getSSOService().authCookie(request, response, ssoToken);
        } else {
            getSSOService().setCookie(request, response, ssoToken);
        }
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, Dict ssoToken) {
        setCookie(request, response, ssoToken, false);
    }

    // ------------------------------- 客户端相关方法 -------------------------------

    /**
     * <p>
     * 获取当前请求 token<br>
     * 该方法直接从 cookie 中解密获取 token, 常使用在登录系统及拦截器中使用 getToken(request)
     * </p>
     * <p>
     * 如果该请求在登录拦截器之后请使用 attrToken(request) 防止二次解密
     * </p>
     *
     * @param request 请求对象
     * @return Dict
     */
    public static Dict getSSOToken(HttpServletRequest request) {
        return getSSOService().getSSOToken(request);
    }

    /**
     * <p>
     * 从请求中获取 token 通过登录拦截器之后使用<br>
     * 该数据为登录拦截器放入 request 中，防止二次解密
     * </p>
     *
     * @param request 访问请求
     * @return Dict
     */
    public static Dict attrToken(HttpServletRequest request) {
        return getSSOService().attrSSOToken(request);
    }

    /**
     * <p>
     * 退出登录， 并且跳至 sso.properties 配置的属性 sso.logout.url 地址
     * </p>
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getSSOService().logout(request, response);
    }

    /**
     * 清理当前登录状态<br>
     * 清理 Cookie、缓存、统计、等数据
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return boolean
     */
    public static boolean clearLogin(HttpServletRequest request, HttpServletResponse response) {
        return getSSOService().clearLogin(request, response);
    }

    /**
     * 退出重定向登录页，跳至 sso.properties 配置的属性 sso.login.url 地址
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    public static void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getSSOService().clearRedirectLogin(request, response);
    }

    /**
     * 获取 token 的缓存主键
     *
     * @param request 当前请求
     * @return String
     */
    public static String getTokenCacheKey(HttpServletRequest request) {
        GXTokenConfigService tokenConfigService = GXSpringContextUtils.getBean(GXTokenConfigService.class);
        assert tokenConfigService != null;
        String platform = Optional.ofNullable(request.getHeader(GXTokenConstant.PLATFORM)).orElse("");
        Dict data = Dict.create().set(GXTokenConstant.PLATFORM, platform);
        Dict loginCredentials = GXCurrentRequestContextUtils.getLoginCredentials(GXTokenConstant.TOKEN_NAME, tokenConfigService.getTokenSecret());
        Long userId = Optional.ofNullable(loginCredentials.getLong(GXTokenConstant.TOKEN_USER_ID_FIELD_NAME)).orElse(0L);
        return tokenConfigService.getTokenCacheKey(userId, data);
    }

    /**
     * 获取 token 的缓存主键
     *
     * @param userId 用户ID
     * @return String
     */
    public static String getTokenCacheKey(Object userId) {
        return GXSSOProperties.toCacheKey(userId);
    }

    /**
     * 踢出 指定用户 ID 的登录用户，退出当前系统。
     *
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean kickLogin(Object userId) {
        return getSSOService().kickLogin(userId);
    }


    /**
     * 解析浏览器端的token
     *
     * @param token token字符串
     * @return 解码之后的token
     */
    public static Dict parser(String token) {
        return parser(token, false);
    }

    /**
     * 解析浏览器端的token
     *
     * @param token  token字符串
     * @param header token字符串是否从header中获取的
     * @return 解码之后的token
     */
    public static Dict parser(String token, boolean header) {
        // 如果是RPC 直接返回
        if (GXCurrentRequestContextUtils.isRPC()) {
            return Dict.create();
        }
        if (CharSequenceUtil.isNotBlank(token) && header) {
            LOGGER.info("token字符串来自于header");
        }
        GXTokenConfigService tokenSecretService = GXSpringContextUtils.getBean(GXTokenConfigService.class);
        if (Objects.isNull(tokenSecretService)) {
            throw new GXBusinessException("请实现GXTokenSecretService类,并将其加入到spring容器中");
        }
        String s = GXAuthCodeUtils.authCodeDecode(token, tokenSecretService.getTokenSecret());
        Dict requestToken = JSONUtil.toBean(s, Dict.class);
        requestToken.put("ip", GXCurrentRequestContextUtils.getClientIP());
        LOGGER.info("SSO组件解析出来的token信息 : {}", requestToken);
        return requestToken;
    }
}