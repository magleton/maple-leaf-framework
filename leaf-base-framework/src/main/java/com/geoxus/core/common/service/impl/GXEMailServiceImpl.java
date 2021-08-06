package com.geoxus.core.common.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.geoxus.core.common.service.GXEMailService;
import com.geoxus.core.common.util.GXCacheKeysUtils;
import com.geoxus.core.common.util.GXRedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class GXEMailServiceImpl implements GXEMailService {
    @Autowired
    private GXCacheKeysUtils gxCacheKeysUtils;

    @Override
    public boolean sendVerifyCode(String email) {
        final String code = RandomUtil.randomString(6);
        final String format = CharSequenceUtil.format("本次修改密码验证码是 : {} , 有效期为5分钟", code);
        final String redisKey = gxCacheKeysUtils.getCacheKey("sys.email.verify.code", email);
        final String sendResult = MailUtil.send(email, "修改密码验证码", format, false);
        if (CharSequenceUtil.isNotBlank(sendResult)) {
            GXRedisUtils.set(redisKey, code, 300, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    @Override
    public boolean verification(String email, String code) {
        final String redisKey = gxCacheKeysUtils.getCacheKey("sys.email.verify.code", email);
        final String value = GXRedisUtils.get(redisKey, String.class);
        if (null != value) {
            if (!CharSequenceUtil.equalsAnyIgnoreCase(code, value)) {
                return false;
            }
            return GXRedisUtils.delete(redisKey);
        }
        return false;
    }
}
