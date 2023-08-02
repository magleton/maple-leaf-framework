package cn.maple.core.datasource.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 软删除数据切面类
 */
@Aspect
@Component
@Slf4j
public class GXMyBatisPlusDeleteSoftAspect {
    @Around("execution(* cn.maple.core.datasource.dao.GXMyBatisDao.deleteSoftCondition(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("发布软删除事件开始");
        Object proceed = point.proceed();
        log.info("发布软删除事件结束");
        return proceed;
    }
}
