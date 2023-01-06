package cn.maple.sso.security.token;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXAuthCodeUtils;
import cn.maple.core.framework.constant.GXTokenConstant;

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
        this.token = GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(extParam), GXTokenConstant.USER_TOKEN_SECRET_KEY);
    }

    @Override
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
