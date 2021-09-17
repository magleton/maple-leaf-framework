package com.geoxus.service.impl;

import com.geoxus.sso.security.token.GXSSOToken;
import com.geoxus.sso.service.GXConfigurableAbstractSSOService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AppConfigurableAbstractSSOServiceImpl extends GXConfigurableAbstractSSOService {
    @Override
    protected GXSSOToken getSSOToken(HttpServletRequest request, String cookieName) {
        System.out.println("MMMKKKLLL");
        return super.getSSOToken(request, cookieName);
    }
}
