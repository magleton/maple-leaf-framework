package com.geoxus.feature.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.feature.annotation.GXRecordHistoryAnnotation;
import com.geoxus.core.datasource.entity.GXBaseEntity;
import com.geoxus.core.framework.exception.GXBusinessException;
import com.geoxus.core.framework.util.GXSpringContextUtil;
import com.geoxus.core.datasource.service.GXDBBaseService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Aspect
@Configuration
@Slf4j
public class GXRecordHistoryAspect {
    @Around("@annotation(com.geoxus.feature.annotation.GXRecordHistoryAnnotation)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result;
        try {
            Object[] args = point.getArgs();
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            final GXRecordHistoryAnnotation gxRecordHistoryAnnotation = method.getAnnotation(GXRecordHistoryAnnotation.class);
            GXDBBaseService<?, ?, ?> service = GXSpringContextUtil.getBean(gxRecordHistoryAnnotation.service());
            if (Objects.nonNull(service)) {
                String[] paramNames = gxRecordHistoryAnnotation.conditionalParameterName();
                String originTableName = gxRecordHistoryAnnotation.originTableName();
                String historyTableName = gxRecordHistoryAnnotation.historyTableName();
                Dict condition = Dict.create();
                Dict dict = Dict.create();
                if (args[0] instanceof GXBaseEntity) {
                    dict = Dict.parse(args[0]);
                } else if (args[0] instanceof Map) {
                    dict = (Dict) args[0];
                }
                for (Map.Entry<String, Object> entry : dict.entrySet()) {
                    String key = CharSequenceUtil.toSymbolCase(entry.getKey(), '_');
                    boolean contains = CollUtil.contains(Arrays.asList(paramNames), key);
                    if (contains) {
                        condition.set(key, entry.getValue());
                    }
                }
                if (condition.isEmpty()) {
                    throw new GXBusinessException("请指定需要记录的历史信息的查询条件");
                }
                service.recordModificationHistory(originTableName, historyTableName, condition, Dict.create().set("append_data", "附加信息"));
            }
            result = point.proceed();
        } catch (Exception e) {
            log.error("record history error", e);
            throw new GXBusinessException("历史信息记录异常");
        }
        return result;
    }
}
