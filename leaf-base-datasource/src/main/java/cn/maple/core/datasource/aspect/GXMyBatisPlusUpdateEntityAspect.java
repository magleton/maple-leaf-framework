package cn.maple.core.datasource.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 更新实体(Entity)切面类
 */
@Aspect
@Component
@Slf4j
public class GXMyBatisPlusUpdateEntityAspect {
    @Around("execution(* com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.update(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("发布更新前的事件开始");
        Object proceed = point.proceed();
        log.info("发布更新后的事件结束");
        return proceed;
    }
}
