package cn.maple.core.framework.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.annotation.GXBusinessLog;
import cn.maple.core.framework.dto.inner.GXBusinessLogDto;
import cn.maple.core.framework.service.GXBusinessLogService;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Aspect
@Slf4j
public class GXBusinessLogAspect implements Ordered {
    /**
     * 定义BusLogAop的切入点为标记@BusLog注解的方法
     */
    @Pointcut(value = "@annotation(cn.maple.core.framework.annotation.GXBusinessLog)")
    public void pointcut() {
    }

    /**
     * 业务操作环绕通知
     *
     * @param proceedingJoinPoint 切点对象
     * @return Object 目标方法执行结果
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        long beginAt = System.currentTimeMillis();
        log.info("----GXBusinessLogAspect 环绕通知 START----");
        // 执行目标方法
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
        // 执行时长(毫秒)
        long executionTime = System.currentTimeMillis() - beginAt;
        saveBusinessLog(proceedingJoinPoint, executionTime);
        return result;
    }

    private void saveBusinessLog(ProceedingJoinPoint proceedingJoinPoint, long executionTime) {
        // 目标方法执行完成后，获取目标类、目标方法上的业务日志注解上的功能名称和功能描述
        Object target = proceedingJoinPoint.getTarget();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        GXBusinessLog clazzAnnotation = target.getClass().getAnnotation(GXBusinessLog.class);
        GXBusinessLog methodAnnotation = signature.getMethod().getAnnotation(GXBusinessLog.class);
        GXBusinessLogDto businessLogDto = new GXBusinessLogDto();
        // 日志的详细描述信息
        String name = methodAnnotation.name();
        if (CharSequenceUtil.isBlank(name) && !Objects.isNull(clazzAnnotation)) {
            name = clazzAnnotation.name();
        }
        String description = methodAnnotation.description();
        businessLogDto.setBusinessName(name);
        businessLogDto.setBusinessDescription(description);
        // 请求的方法名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        businessLogDto.setMethodName(className + "." + methodName + "()");
        // 请求的参数
        Object[] args = proceedingJoinPoint.getArgs();
        String params = JSONUtil.toJsonStr(args);
        businessLogDto.setParams(params);
        // 设置IP地址
        businessLogDto.setIp(GXCurrentRequestContextUtils.getClientIP());
        // 执行时间
        businessLogDto.setExecutionTime(executionTime);
        // 请求时间
        businessLogDto.setRequestAt(DateUtil.currentSeconds());

        // 调用自定义的业务方法
        GXBusinessLogService businessLogService = GXSpringContextUtils.getBean(GXBusinessLogService.class);
        if (!Objects.isNull(businessLogService)) {
            // 用户名
            String username = businessLogService.getUserName();
            businessLogDto.setUserName(username);
            // 保存业务日志数据
            businessLogService.saveBusinessLog(businessLogDto);
        }
        log.info("----GXBusinessLogAspect 环绕通知 END----");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
