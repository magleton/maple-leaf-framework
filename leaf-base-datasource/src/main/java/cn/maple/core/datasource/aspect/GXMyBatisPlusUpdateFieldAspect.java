package cn.maple.core.datasource.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 更新指定字段切面类
 */
@Aspect
@Component
@Slf4j
public class GXMyBatisPlusUpdateFieldAspect {
    @Around("execution(* cn.maple.core.datasource.mapper.GXBaseMapper.updateFieldByCondition(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("发布更新数据库指定字段事件开始");
        Object proceed = point.proceed();
        log.info("发布更新数据库指定字段事件结束");
        return proceed;
    }
}
