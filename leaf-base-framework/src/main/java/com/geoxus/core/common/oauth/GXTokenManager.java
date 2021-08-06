package com.geoxus.core.common.oauth;

import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.constant.GXTokenConstants;
import com.geoxus.core.common.util.GXAuthCodeUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Slf4j
public class GXTokenManager {
    @GXFieldCommentAnnotation(zhDesc = "管理员的token缓存组件")
    private static final Cache<String, Dict> ADMIN_TOKEN_CACHE = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(Duration.ofMinutes(24)).build();

    private GXTokenManager() {
    }

    /**
     * 生成ADMIN后台登录的token
     *
     * <pre>
     *     {@code
     *     generateUserToken(1 , Dict.create().set("phone" , "13800138000"))
     *     }
     * </pre>
     *
     * @param adminId 管理员ID
     * @param param   附加信息
     * @return String
     */
    public static String generateAdminToken(long adminId, Dict param) {
        param.putIfAbsent(GXTokenConstants.ADMIN_ID, adminId);
        param.putIfAbsent("platform", "GEO_XUS");
        return GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(param), GXTokenConstants.KEY, GXTokenConstants.ADMIN_EXPIRES_REFRESH);
    }

    /**
     * 生成前端用户的登录Token
     *
     * <pre>
     *     {@code
     *     generateUserToken(1 , Dict.create().set("phone" , "13800138000"))
     *     }
     * </pre>
     *
     * @param userId 前端用户的ID
     * @param param  附加信息
     * @return String
     */
    public static String generateUserToken(long userId, Dict param) {
        param.putIfAbsent(GXTokenConstants.USER_ID, userId);
        return GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(param), GXTokenConstants.KEY);
    }

    /**
     * 解码前端用户的TOKEN字符串
     * 注意: 由于前端用户需要保持长时间的登录信息, 在生成token字符串时, 不需要指定token的过期时间, 过期时间时存放在s_user_token表中进行维护
     *
     * @param source 加密TOKEN字符串
     * @return Dict
     */
    public static Dict decodeUserToken(String source) {
        try {
            String s = GXAuthCodeUtils.authCodeDecode(source, GXTokenConstants.KEY);
            return JSONUtil.toBean(s, Dict.class);
        } catch (Exception e) {
            return Dict.create();
        }
    }

    /**
     * 解码后端用户的加密TOKEN字符串
     * 注意: 由于后端用户不需要保持长时间的登录操作, 所以在生成token时, 为token指定了过期时间
     *
     * @param source 加密TOKEN字符串
     * @return Dict
     */
    public static Dict decodeAdminToken(String source) {
        try {
            String cacheKey = SecureUtil.md5(source);
            return ADMIN_TOKEN_CACHE.get(cacheKey, () -> {
                String s = GXAuthCodeUtils.authCodeDecode(source, GXTokenConstants.KEY);
                Dict dict = JSONUtil.toBean(s, Dict.class);
                if (dict.isEmpty()) {
                    return Dict.create();
                }
                return dict;
            });
        } catch (JSONException | ExecutionException exception) {
            log.error(exception.getMessage());
        }
        return Dict.create();
    }
}
