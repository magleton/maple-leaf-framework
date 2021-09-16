package com.geoxus.sso.plugins.impl;

import com.geoxus.sso.plugins.GXSsoPlugin;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class GXLoginSsoPlugin implements GXSsoPlugin {
    @Override
    public boolean login(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }
}
