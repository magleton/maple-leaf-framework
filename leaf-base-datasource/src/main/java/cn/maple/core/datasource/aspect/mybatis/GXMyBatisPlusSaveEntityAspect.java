package cn.maple.core.datasource.aspect.mybatis;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.datasource.annotation.GXMyBatisListener;
import cn.maple.core.datasource.constant.GXMyBatisEventConstant;
import cn.maple.core.datasource.enums.GXModelEventNamingEnums;
import cn.maple.core.datasource.event.GXMyBatisModelSaveEntityEvent;
import cn.maple.core.datasource.service.GXMybatisListenerService;
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
    @Around("target(cn.maple.core.datasource.mapper.GXBaseMapper) && execution(* insert(..))")
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
                GXMyBatisListener myBatisListener = AnnotationUtil.getAnnotation(mapper, GXMyBatisListener.class);
                Class<? extends GXMybatisListenerService> aClass = myBatisListener.listenerClazz();
                String eventType = GXModelEventNamingEnums.SYNC_SAVE_ENTITY.getEventType();
                String eventName = GXModelEventNamingEnums.SYNC_SAVE_ENTITY.getEventName();
                String runType = myBatisListener.runType();
                if (runType.equals(GXMyBatisEventConstant.MYBATIS_ASYNC_EVENT)) {
                    eventType = GXModelEventNamingEnums.ASYNC_SAVE_ENTITY.getEventType();
                    eventName = GXModelEventNamingEnums.ASYNC_SAVE_ENTITY.getEventName();
                }
                Dict eventParam = Dict.create().set("listenerClazzName", aClass.getSimpleName()).set("listenerClazz", aClass);
                GXMyBatisModelSaveEntityEvent<Dict> saveEntityEvent = new GXMyBatisModelSaveEntityEvent<>(source, eventType, eventParam, eventName);
                GXEventPublisherUtils.publishEvent(saveEntityEvent);
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
