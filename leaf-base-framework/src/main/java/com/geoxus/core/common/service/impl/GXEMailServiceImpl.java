package com.geoxus.core.common.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.geoxus.core.common.service.GXEMailService;
import com.geoxus.core.common.util.GXCacheKeysUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GXEMailServiceImpl implements GXEMailService {
    @Resource
    private GXCacheKeysUtils gxCacheKeysUtils;

    @Override
    public boolean sendVerifyCode(String email) {
        final String code = RandomUtil.randomString(6);
        final String format = CharSequenceUtil.format("本次修改密码验证码是 : {} , 有效期为5分钟", code);
        final String redisKey = gxCacheKeysUtils.getCacheKey("sys.email.verify.code", email);
        final String sendResult = MailUtil.send(email, "修改密码验证码", format, false);
        // GXRedisUtils.set(redisKey, code, 300, TimeUnit.SECONDS);
        return CharSequenceUtil.isNotBlank(sendResult);
    }

    @Override
    public boolean verification(String email, String code) {
        final String redisKey = gxCacheKeysUtils.getCacheKey("sys.email.verify.code", email);
        final String value = "hello";//GXRedisUtils.get(redisKey, String.class);
        if (null != value) {
            return CharSequenceUtil.equalsAnyIgnoreCase(code, value);//GXRedisUtils.delete(redisKey);
        }
        return false;
    }
}
