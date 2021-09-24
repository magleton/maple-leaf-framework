package com.geoxus.commons.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.common.pojo.GXResultCode;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.commons.annotation.GXCheckCaptchaAnnotation;
import com.geoxus.core.common.constant.GXCommonConstant;
import com.geoxus.core.common.service.GXCaptchaService;
import com.geoxus.core.common.service.GXSendSMSService;
import com.geoxus.core.common.util.GXFrameworkCommonUtils;
import com.geoxus.core.common.util.GXHttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class GXCheckCaptchaAspect {
    @Pointcut("@annotation(com.geoxus.commons.annotation.GXCheckCaptchaAnnotation)")
    public void checkCaptchaPointCut() {
    }

    @Around("checkCaptchaPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        final GXCheckCaptchaAnnotation gxCheckCaptchaAnnotation = method.getAnnotation(GXCheckCaptchaAnnotation.class);
        final boolean annotationValue = gxCheckCaptchaAnnotation.value();
        final int verifyType = gxCheckCaptchaAnnotation.verifyType();
        Dict param = Convert.convert(Dict.class, point.getArgs()[0]);
        final List<String> specialPhone = Convert.convert(new TypeReference<List<String>>() {
        }, Optional.ofNullable(GXFrameworkCommonUtils.getEnvironmentValue("special.phone", Object.class)).orElse(Collections.emptyList()));
        if (!annotationValue) {
            return point.proceed(point.getArgs());
        }
        if (verifyType == 0) {
            throw new GXBusinessException("请传递验证码类型");
        }
        String verifyCode = param.getStr("verify_code");
        if (null == verifyCode) {
            verifyCode = GXHttpContextUtils.getHttpParam("verify_code", String.class);
        }
        if (null == verifyCode) {
            String msg = "请传递手机验证码";
            if (verifyType == GXCommonConstant.CAPTCHA_VERIFY) {
                msg = "请传递图形验证码";
            }
            throw new GXBusinessException(msg);
        }
        if (verifyType == GXCommonConstant.SMS_VERIFY) {
            String phone = param.getStr("phone");
            if (null == phone) {
                throw new GXBusinessException("请传递手机号码");
            }
            phone = GXFrameworkCommonUtils.decryptedPhoneNumber(phone);
            final String specialVerifyCode = GXFrameworkCommonUtils.getEnvironmentValue("special.verify_code", String.class, "");
            if (CollUtil.contains(specialPhone, phone) && verifyCode.equals(specialVerifyCode)) {
                log.info(StrUtil.format("正在使用特殊号码进行验证 : {}-{}", phone, specialVerifyCode));
                return point.proceed(point.getArgs());
            }
            if (!getSendSMSService().verification(phone, verifyCode)) {
                throw new GXBusinessException(GXResultCode.SMS_CAPTCHA_ERROR);
            }
        } else if (verifyType == GXCommonConstant.CAPTCHA_VERIFY) {
            String uuid = param.getStr("uuid");
            if (null == uuid) {
                uuid = GXHttpContextUtils.getHttpParam("uuid", String.class);
            }
            if (null == uuid) {
                throw new GXBusinessException("请传递图形验证码标识uuid");
            }
            if (!getCaptchaService().checkCaptcha(uuid, verifyCode)) {
                throw new GXBusinessException(GXResultCode.GRAPH_CAPTCHA_ERROR);
            }
        }
        return point.proceed(point.getArgs());
    }

    private GXSendSMSService getSendSMSService() {
        return GXSpringContextUtil.getBean(GXSendSMSService.class);
    }

    private GXCaptchaService getCaptchaService() {
        return GXSpringContextUtil.getBean(GXCaptchaService.class);
    }
}
