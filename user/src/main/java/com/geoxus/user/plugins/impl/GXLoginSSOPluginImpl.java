package com.geoxus.plugins.impl;

import com.geoxus.sso.plugins.GXSSOPlugin;
import com.geoxus.sso.security.token.GXSSOToken;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class GXLoginSSOPluginImpl implements GXSSOPlugin {
    @Override
    public boolean login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("AAAAAA");
        return false;
    }

    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    @Override
    public boolean validateToken(GXSSOToken ssoToken) {
        System.out.println("验证token合法性");
        return true;
    }
}
