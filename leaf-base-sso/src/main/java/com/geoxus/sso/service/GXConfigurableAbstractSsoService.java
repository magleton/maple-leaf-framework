package com.geoxus.sso.service;

import com.geoxus.sso.config.GXSsoConfig;

/**
 * <p>
 * SSO 单点登录服务抽象实现类
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public class GXConfigurableAbstractSsoService extends GXAbstractSsoService {
    public GXConfigurableAbstractSsoService() {
        config = GXSsoConfig.getInstance();
    }
}
