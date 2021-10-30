package cn.maple.sso.security.token;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXAuthCodeUtil;
import cn.maple.core.framework.constant.GXTokenConstant;

/**
 * 刷新票据
 *
 * @author britton
 * @since 2021-09-16
 */
public class GXRefreshToken implements GXToken {
    private final String token;

    private GXRefreshToken(String userId, Dict extParam) {
        token = GXAuthCodeUtil.authCodeEncode(JSONUtil.toJsonStr(extParam), GXTokenConstant.KEY);
    }

    @Override
    public String getToken() {
        return null;
    }

    public String getSubject() {
        return token;
    }
}
