package com.geoxus.shiro.oauth;

import org.apache.shiro.authc.AuthenticationToken;

public class GXOAuth2Token implements AuthenticationToken {
    private final String token;

    public GXOAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}