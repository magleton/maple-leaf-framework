package cn.maple.core.framework.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.exception.GXTokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

@Slf4j
public class GXTokenManagerUtils {
    private GXTokenManagerUtils() {
    }

    /**
     * 生成ADMIN后台登录的token
     *
     * <pre>
     *     {@code
     *     generateUserToken(1 , Dict.create().set("phone" , "13800138000"),"df5386b5e634" , 120)
     *     }
     * </pre>
     *
     * @param adminId   管理员ID
     * @param param     附加信息
     * @param secretKey 加解密key
     * @param expires   过期时间
     * @return String
     */
    public static String generateAdminToken(Object adminId, Dict param, String secretKey, int expires) {
        param.putIfAbsent(GXTokenConstant.TOKEN_ADMIN_ID_FIELD_NAME, adminId);
        param.putIfAbsent(GXTokenConstant.LOGIN_AT_FIELD_NAME, DateUtil.currentSeconds());
        param.putIfAbsent("platform", GXTokenConstant.PLATFORM);
        return GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(param), secretKey, expires);
    }

    /**
     * 解码后端用户的加密TOKEN字符串
     * 注意: 由于后端用户不需要保持长时间的登录操作, 所以在生成token时, 为token指定了过期时间
     *
     * @param source    加密TOKEN字符串
     * @param secretKey 加解密KEY
     * @return Dict
     */
    public static Dict decodeAdminToken(String source, String secretKey) {
        try {
            String s = GXAuthCodeUtils.authCodeDecode(source, secretKey);
            if (CharSequenceUtil.equalsIgnoreCase("{}", s)) {
                throw new GXTokenInvalidException("无效用户身份!!!");
            }
            return JSONUtil.toBean(s, Dict.class);
        } catch (JSONException exception) {
            log.error(exception.getMessage());
        }
        return Dict.create();
    }

    /**
     * 生成前端用户的登录Token
     *
     * <pre>
     *     {@code
     *     generateUserToken(1 , Dict.create().set("phone" , "13800138000") , "5e6344e6c264f7601802c6" , 600)
     *     }
     * </pre>
     *
     * @param userId    前端用户的ID
     * @param param     附加信息
     * @param secretKey 加解密KEY
     * @param expires   过期时间
     * @return String
     */
    public static String generateUserToken(Object userId, Dict param, String secretKey, int expires) {
        param.putIfAbsent(GXTokenConstant.TOKEN_USER_ID_FIELD_NAME, userId);
        param.putIfAbsent(GXTokenConstant.LOGIN_AT_FIELD_NAME, DateUtil.currentSeconds());
        param.putIfAbsent("platform", GXTokenConstant.PLATFORM);
        return GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(param), secretKey, expires);
    }

    /**
     * 解码前端用户的TOKEN字符串
     * 注意: 由于前端用户需要保持长时间的登录信息, 在生成token字符串时, 不需要指定token的过期时间, 过期时间时存放在s_user_token表中进行维护
     *
     * @param source    加密TOKEN字符串
     * @param secretKey 加解密KEY
     * @return Dict
     */
    public static Dict decodeUserToken(String source, String secretKey) {
        try {
            String s = GXAuthCodeUtils.authCodeDecode(source, secretKey);
            if (CharSequenceUtil.equalsIgnoreCase("{}", s)) {
                throw new GXTokenInvalidException("无效用户身份!!!");
            }
            return JSONUtil.toBean(s, Dict.class);
        } catch (Exception e) {
            return Dict.create();
        }
    }

    /**
     * 验证token的有效性
     * 验证规则可以调用别的服务
     * 亦可以自身验证
     * 自身验证可以通过redis的token缓存key+tokenSecret来进行验证解密 并验证是否有效
     * 减少服务间的通信
     *
     * @return true 有效 ; false 无效
     */
    public static boolean verifyTokenEffectiveness() {
        try {
            Object bean = GXSpringContextUtils.getBean(ClassUtils.forName("cn.maple.sso.service.GXTokenConfigService", GXTokenManagerUtils.class.getClassLoader()));
            Object verifyTokenEffectiveness = GXCommonUtils.reflectCallObjectMethod(bean, "verifyTokenEffectiveness");
            if (ObjectUtil.isNull(verifyTokenEffectiveness) && Boolean.FALSE.equals(verifyTokenEffectiveness)) {
                throw new GXTokenInvalidException("无效的用户身份!");
            }
            return Boolean.TRUE;
        } catch (ClassNotFoundException e) {
            throw new GXBusinessException(e.getMessage(), e);
        }
    }
}