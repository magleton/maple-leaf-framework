package cn.maple.core.datasource.aspect;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.datasource.annotation.GXMyBatisEvent;
import cn.maple.core.datasource.enums.GXModelEventNamingEnums;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.util.GXEventPublisherUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * 保存实体(Entity)切面类
 */
@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class GXMyBatisPlusSaveEntityAspect {
    @Around("execution(* com.baomidou.mybatisplus.core.mapper.BaseMapper.save(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("发布创建数据库事件开始");
        Object proceed = point.proceed();
        publishEvent(point);
        log.info("发布创建数据库事件结束");
        return proceed;
    }

    /**
     * 处理切点的参数
     *
     * @param type  类型
     * @param point 切点对象
     */
    private Dict handlePointArgs(Type type, ProceedingJoinPoint point) {
        Dict retDict = Dict.create();
        Class<Mapper> mapper = convertTypeToMapper(type);
        if (ObjectUtil.isNotNull(mapper)) {
            Object[] args = point.getArgs();
            retDict = Convert.convert(Dict.class, args[0]);
        }
        return retDict;
    }

    /**
     * 发布事件
     *
     * @param point 切点对象
     */
    private void publishEvent(ProceedingJoinPoint point) {
        Type[] myBatisMapper = AopUtils.getTargetClass(point.getTarget()).getInterfaces();
        for (Type type : myBatisMapper) {
            Class<Mapper> mapper = convertTypeToMapper(type);
            if (ObjectUtil.isNotNull(mapper)) {
                Dict source = handlePointArgs(type, point);
                GXMyBatisEvent myBatisEvent = AnnotationUtil.getAnnotation(mapper, GXMyBatisEvent.class);
                Class<? extends GXBaseEvent<?>> aClass = myBatisEvent.eventClass();
                String scene = mapper.getSimpleName();
                String eventName = GXModelEventNamingEnums.SAVE_ENTITY.getEventName();
                // 使用 GXBaseEvent(T source, String eventName, String scene) 构造函数
                GXBaseEvent<?> updateFieldEvent = ReflectUtil.newInstance(aClass, source, eventName, scene);
                GXEventPublisherUtils.publishEvent(updateFieldEvent);
            }
        }
    }

    /**
     * 将Type转换为Mapper
     *
     * @param type 待转换的对象
     */
    private Class<Mapper> convertTypeToMapper(Type type) {
        return Convert.convert(new TypeReference<>() {
        }, type);
    }
}
