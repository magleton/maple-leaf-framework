package com.geoxus.plugins.impl;

import com.geoxus.sso.plugins.GXSsoPlugin;
import com.geoxus.sso.security.token.GXSsoToken;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class GXLoginSsoPluginImpl implements GXSsoPlugin {
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
    public boolean validateToken(GXSsoToken ssoToken) {
        System.out.println("验证token合法性");
        return true;
    }
}
