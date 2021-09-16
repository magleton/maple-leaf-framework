package com.geoxus.sso.security.token;

import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.common.util.GXAuthCodeUtil;
import com.geoxus.core.common.constant.GXTokenConstant;
import com.geoxus.sso.config.GXSsoConfig;
import com.geoxus.sso.constant.GXSsoConstant;
import com.geoxus.sso.enums.GXTokenFlag;
import com.geoxus.sso.enums.GXTokenOrigin;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
    private String userId;

    /**
     * 租户 ID
     */
    private String tenantId;

    /**
     * 发布者
     */
    private String isSuer;

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

    public static GXSsoToken create() {
        return new GXSsoToken();
    }

    public static GXSsoToken parser(String token) {
        return parser(token, false);
    }

    public static GXSsoToken parser(String token, boolean header) {
        String s = GXAuthCodeUtil.authCodeDecode(token, GXTokenConstant.KEY);
        return JSONUtil.toBean(s, GXSsoToken.class);
    }

    @Override
    public String getToken() {
        Dict param = Dict.create();
        if (null != this.getUserId()) {
            param.set("userId", this.getUserId());
        }
        if (null != this.getTenantId()) {
            param.set(GXSsoConstant.TOKEN_TENANT_ID, this.getTenantId());
        }
        if (null != this.getIp()) {
            param.set(GXSsoConstant.TOKEN_USER_IP, this.getIp());
        }
        if (null != this.getIsSuer()) {
            param.set("isSuer", this.getIsSuer());
        }
        if (null != this.getUserAgent()) {
            param.set(GXSsoConstant.TOKEN_USER_AGENT, this.getUserAgent());
        }
        if (GXTokenFlag.NORMAL != this.getFlag()) {
            param.set(GXSsoConstant.TOKEN_FLAG, this.getFlag().value());
        }
        if (GXTokenOrigin.COOKIE != this.getOrigin()) {
            param.set(GXSsoConstant.TOKEN_ORIGIN, this.getOrigin().value());
        }
        param.set("isSuedAt", new Date(time));
        return GXAuthCodeUtil.authCodeEncode(JSONUtil.toJsonStr(param), GXTokenConstant.KEY);
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

    public String getUserId() {
        return userId;
    }

    public GXSsoToken setUserId(Object userId) {
        this.userId = String.valueOf(userId);
        return this;
    }

    public GXSsoToken setUserId(String userId) {
        this.userId = userId;
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

    public String getIsSuer() {
        return isSuer;
    }

    public GXSsoToken setIsSuer(String isSuer) {
        this.isSuer = isSuer;
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
        return GXSsoConfig.toCacheKey(this.getUserId());
    }
}
