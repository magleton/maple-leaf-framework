package com.geoxus.sso.security.token;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.framework.util.GXAuthCodeUtil;
import com.geoxus.core.framework.constant.GXTokenConstant;

import java.io.Serializable;

/**
 * <p>
 * 访问票据
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public class GXAccessToken implements GXToken, Serializable {
    private String token;

    public GXAccessToken() {
        // TO DO NOTHING
    }

    public GXAccessToken(Dict extParam) {
        this.token = GXAuthCodeUtil.authCodeEncode(JSONUtil.toJsonStr(extParam), GXTokenConstant.KEY);
    }

    @Override
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
