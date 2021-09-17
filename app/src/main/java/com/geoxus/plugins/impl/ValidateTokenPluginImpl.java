package com.geoxus.plugins.impl;

import com.geoxus.sso.plugins.GXSsoPlugin;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ValidateTokenPluginImpl implements GXSsoPlugin {
    @Override
    public boolean login(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }
}
