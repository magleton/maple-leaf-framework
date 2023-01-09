package cn.maple.sso.oauth;

import cn.hutool.core.lang.Dict;

/**
 * <p>
 * SSO 权限授权接口
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public interface GXSSOAuthorization {
    boolean isPermitted(Dict token, String permission);
}