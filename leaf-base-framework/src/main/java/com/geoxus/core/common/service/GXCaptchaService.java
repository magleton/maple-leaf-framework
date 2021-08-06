package com.geoxus.core.common.service;

import cn.hutool.core.lang.Dict;

import java.util.Map;

/**
 * 生成验证码接口
 */
public interface GXCaptchaService {
    /**
     * 生成验证码
     *
     * @param param 参数
     * @return Map<String, Object>
     */
    Map<String, Object> getCaptcha(Dict param);

    /**
     * 验证验证码
     *
     * @param uuid UUID标识
     * @param code 验证码
     * @return boolean
     */
    boolean checkCaptcha(String uuid, String code);
}
