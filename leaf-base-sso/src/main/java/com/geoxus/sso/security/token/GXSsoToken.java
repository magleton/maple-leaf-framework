package com.geoxus.sso.security.token;

import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.sso.config.GXSsoConfig;
import com.geoxus.sso.enums.GXTokenFlag;
import com.geoxus.sso.enums.GXTokenOrigin;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * SSO Token
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
@Slf4j
public class GXSsoToken extends GXAccessToken {
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
    private String id;

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
    private Object data;

    public GXSsoToken() {
        // TO DO NOTHING
    }

    public static GXSsoToken parser(String token) {
        return parser(token, false);
    }

    public static GXSsoToken parser(String token, boolean header) {
        String tenantId = "90";
        GXSsoToken ssoToken = new GXSsoToken();
        ssoToken.setTenantId(tenantId);
        return ssoToken;
    }

    @Override
    public String getToken() {
        long userId = 1L;
        Dict param = Dict.create();
        return GXTokenManager.generateUserToken(userId, param);
    }

    public GXTokenFlag getFlag() {
        return flag;
    }

    public GXSsoToken setFlag(GXTokenFlag flag) {
        this.flag = flag;
        return this;
    }

    public GXTokenOrigin getOrigin() {
        return origin;
    }

    public GXSsoToken setOrigin(GXTokenOrigin origin) {
        this.origin = origin;
        return this;
    }

    public String getId() {
        return id;
    }

    public GXSsoToken setId(Object id) {
        this.id = String.valueOf(id);
        return this;
    }

    public GXSsoToken setId(String id) {
        this.id = id;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public GXSsoToken setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public GXSsoToken setTenantId(Object tenantId) {
        this.tenantId = String.valueOf(tenantId);
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public GXSsoToken setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public GXSsoToken setIp(HttpServletRequest request) {
        this.ip = ServletUtil.getClientIP(request);
        return this;
    }

    public GXSsoToken setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public GXSsoToken setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public GXSsoToken setUserAgent(HttpServletRequest request) {
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

    public GXSsoToken setTime(long time) {
        this.time = time;
        return this;
    }

    public Object getData() {
        return data;
    }

    public GXSsoToken setData(Object data) {
        this.data = data;
        return this;
    }

    public String toCacheKey() {
        return GXSsoConfig.toCacheKey(this.getId());
    }
}
