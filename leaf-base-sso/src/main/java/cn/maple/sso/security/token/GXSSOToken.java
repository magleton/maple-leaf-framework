package cn.maple.sso.security.token;

import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXAuthCodeUtils;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.sso.constant.GXSSOConstant;
import cn.maple.sso.enums.GXTokenFlag;
import cn.maple.sso.enums.GXTokenOrigin;
import cn.maple.sso.properties.GXSSOProperties;
import cn.maple.sso.service.GXTokenConfigService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

/**
 * SSO Token
 * <p>
 * 将其实现类放入Spring容器中进行管理
 * <p>
 * {@code
 *
 * @author britton britton@126.com
 * @Slf4j
 * @Component("loginSSOPlugin") public class GXLoginSSOPluginImpl implements GXSSOPlugin {}
 * }
 * @since 2021-09-16
 */
@Slf4j
public class GXSSOToken extends GXAccessToken {
    /**
     * token 标记
     */
    private GXTokenFlag flag = GXTokenFlag.NORMAL;

    /**
     * token来源
     */
    private GXTokenOrigin origin = GXTokenOrigin.COOKIE;

    /**
     * 主键
     */
    private String userId;

    /**
     * 租户 ID
     */
    private String tenantId;

    /**
     * 发布者
     */
    private String issuer;

    /**
     * IP 地址
     */
    private String ip;

    /**
     * 创建日期
     */
    private long time = System.currentTimeMillis();

    /**
     * 请求头信息
     */
    private String userAgent;

    /**
     * 预留扩展、配合缓存使用
     */
    private transient Object data;

    /**
     * 自定义的登录时间
     */
    private Long loginAt;

    public GXSSOToken() {
        // TO DO NOTHING
    }

    public static GXSSOToken create() {
        return new GXSSOToken();
    }

    public static GXSSOToken parser(String token) {
        return parser(token, false);
    }

    public static GXSSOToken parser(String token, boolean header) {
        GXTokenConfigService tokenSecretService = GXSpringContextUtils.getBean(GXTokenConfigService.class);
        if (Objects.isNull(tokenSecretService)) {
            throw new GXBusinessException("请实现GXTokenSecretService类,并将其加入到spring容器中");
        }
        String s = GXAuthCodeUtils.authCodeDecode(token, tokenSecretService.getTokenSecret());
        GXSSOToken ssoToken = JSONUtil.toBean(s, GXSSOToken.class);
        ssoToken.setIp(GXCurrentRequestContextUtils.getHttpServletRequest());
        return ssoToken;
    }

    @Override
    public String getToken() {
        String token = GXCurrentRequestContextUtils.getHeader(GXTokenConstant.TOKEN_NAME);
        if (Objects.nonNull(token)) {
            return token;
        }
        Dict param = Dict.create();
        if (null != this.getUserId()) {
            param.set("userId", this.getUserId());
        }
        if (null != this.getTenantId()) {
            param.set(GXSSOConstant.TOKEN_TENANT_ID, this.getTenantId());
        }
        if (null != this.getIp()) {
            param.set(GXSSOConstant.TOKEN_USER_IP, this.getIp());
        }
        if (null != this.getIssuer()) {
            param.set("issuer", this.getIssuer());
        }
        if (null != this.getUserAgent()) {
            param.set(GXSSOConstant.TOKEN_USER_AGENT, this.getUserAgent());
        }
        if (GXTokenFlag.NORMAL != this.getFlag()) {
            param.set(GXSSOConstant.TOKEN_FLAG, this.getFlag().value());
        }
        if (GXTokenOrigin.COOKIE != this.getOrigin()) {
            param.set(GXSSOConstant.TOKEN_ORIGIN, this.getOrigin().value());
        }
        param.set("time", time);
        param.set("issuedAt", new Date(time));
        GXTokenConfigService tokenSecretService = GXSpringContextUtils.getBean(GXTokenConfigService.class);
        if (Objects.isNull(tokenSecretService)) {
            throw new GXBusinessException("请实现GXTokenSecretService,并将其放入spring容器中");
        }
        String tokenSecret = tokenSecretService.getTokenSecret();
        return GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(param), tokenSecret);
    }

    public GXTokenFlag getFlag() {
        return flag;
    }

    public void setFlag(GXTokenFlag flag) {
        this.flag = flag;
    }

    public GXTokenOrigin getOrigin() {
        return origin;
    }

    public GXSSOToken setOrigin(GXTokenOrigin origin) {
        this.origin = origin;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public GXSSOToken setUserId(Object userId) {
        this.userId = String.valueOf(userId);
        return this;
    }

    public GXSSOToken setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public GXSSOToken setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public GXSSOToken setTenantId(Object tenantId) {
        this.tenantId = String.valueOf(tenantId);
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public GXSSOToken setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public GXSSOToken setIp(HttpServletRequest request) {
        this.ip = ServletUtil.getClientIP(request);
        return this;
    }

    public GXSSOToken setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public GXSSOToken setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public GXSSOToken setUserAgent(HttpServletRequest request) {
        String userAgent = SecureUtil.md5(ServletUtil.getHeader(request, "user-agent", StandardCharsets.UTF_8));
        if (null == userAgent) {
            return null;
        }
        this.userAgent = userAgent.substring(3, 8);
        return this;
    }

    public long getTime() {
        return time;
    }

    public GXSSOToken setTime(long time) {
        this.time = time;
        return this;
    }

    public Object getData() {
        return data;
    }

    public GXSSOToken setData(Object data) {
        this.data = data;
        return this;
    }

    public String toCacheKey() {
        return GXSSOProperties.toCacheKey(this.getUserId());
    }

    public Long getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Long loginAt) {
        this.loginAt = loginAt;
    }
}
