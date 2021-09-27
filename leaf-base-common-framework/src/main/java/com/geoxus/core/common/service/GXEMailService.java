package com.geoxus.core.common.service;

public interface GXEMailService {
    /**
     * 发送邮件验证码
     *
     * @param email 用户电子邮箱
     * @return
     */
    boolean sendVerifyCode(String email);

    /**
     * 验证验证码
     *
     * @param email 用户电子邮箱
     * @param code  验证码
     * @return boolean
     */
    boolean verification(String email, String code);
}
