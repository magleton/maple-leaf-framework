package com.geoxus.feature.aspect;

import com.geoxus.core.framework.constant.GXTokenConstant;
import com.geoxus.core.framework.pojo.GXResultCode;
import com.geoxus.core.framework.util.GXResultUtil;
import com.geoxus.core.framework.util.GXHttpContextUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class GXUploadFileLegalAspect {
    @Pointcut("@annotation(com.geoxus.feature.annotation.GXUploadFileLegalAnnotation)")
    public void uploadFileLegalPointCut() {
    }

    @Around("uploadFileLegalPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = GXHttpContextUtils.getHttpServletRequest();
        assert request != null;
        if (null != request.getHeader(GXTokenConstant.ADMIN_TOKEN) || null != request.getHeader(GXTokenConstant.USER_TOKEN)) {
            return point.proceed();
        }
        return GXResultUtil.error(GXResultCode.NEED_PERMISSION);
    }
}
