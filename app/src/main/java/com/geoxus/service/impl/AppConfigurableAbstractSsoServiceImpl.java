package com.geoxus.service.impl;

import com.geoxus.sso.properties.GXSsoConfigProperties;
import com.geoxus.sso.security.token.GXSsoToken;
import com.geoxus.sso.service.GXConfigurableAbstractSsoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class AppConfigurableAbstractSsoServiceImpl extends GXConfigurableAbstractSsoService {
    @Resource
    private GXSsoConfigProperties ssoConfigProperties;

    public AppConfigurableAbstractSsoServiceImpl() {
        this.config = ssoConfigProperties.getConfig();
    }

    @Override
    protected GXSsoToken getSsoToken(HttpServletRequest request, String cookieName) {
        System.out.println("MMMKKKLLL");
        return super.getSsoToken(request, cookieName);
    }
}
