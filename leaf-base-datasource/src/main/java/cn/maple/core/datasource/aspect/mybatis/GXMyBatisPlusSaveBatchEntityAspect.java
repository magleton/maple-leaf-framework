package cn.maple.core.datasource.aspect.mybatis;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.datasource.annotation.GXMyBatisListener;
import cn.maple.core.datasource.constant.GXMyBatisEventConstant;
import cn.maple.core.datasource.enums.GXModelEventNamingEnums;
import cn.maple.core.datasource.event.GXMyBatisModelSaveBatchEntityEvent;
import cn.maple.core.datasource.service.GXMybatisListenerService;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXEventPublisherUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 更新实体(Entity)切面类
 */
@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class GXMyBatisPlusSaveBatchEntityAspect {
    @Around("target(com.baomidou.mybatisplus.extension.service.impl.ServiceImpl) && (execution(* saveBatch(..)) || execution(* saveOrUpdateBatch(..)))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.debug("发布批量更新前的事件开始");
        Object proceed = point.proceed();
        publishEvent(point);
        log.debug("发布批量更新后的事件结束");
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
            Collection<Object> entity = (Collection<Object>) args[0];
            Collection<Dict> entityData = Convert.convert(new TypeReference<List>() {
            }, entity);
            retDict.set("entityData", entityData);
        }
        return retDict;
    }

    /**
     * 发布事件
     *
     * @param point 切点对象
     */
    private void publishEvent(ProceedingJoinPoint point) {
        Type myBatisMapper = GXCommonUtils.getGenericClassType(AopUtils.getTargetClass(point.getTarget()), 0);//AopUtils.getTargetClass(point.getTarget()).getInterfaces();
        Class<Mapper> mapper = convertTypeToMapper(myBatisMapper);
        if (ObjectUtil.isNotNull(mapper)) {
            GXMyBatisListener myBatisListener = AnnotationUtil.getAnnotation(mapper, GXMyBatisListener.class);
            if (ObjectUtil.isNull(myBatisListener)) {
                return;
            }
            Dict source = handlePointArgs(myBatisMapper, point);
            Class<? extends GXMybatisListenerService> aClass = myBatisListener.listenerClazz();
            String eventType = GXModelEventNamingEnums.SYNC_SAVE_BATCH_ENTITY.getEventType();
            String eventName = GXModelEventNamingEnums.SYNC_SAVE_BATCH_ENTITY.getEventName();
            Dict eventParam = Dict.create().set("listenerClazzName", aClass.getSimpleName()).set("listenerClazz", aClass);
            String runType = myBatisListener.runType();
            if (runType.equals(GXMyBatisEventConstant.MYBATIS_ASYNC_EVENT)) {
                eventType = GXModelEventNamingEnums.ASYNC_SAVE_BATCH_ENTITY.getEventType();
                eventName = GXModelEventNamingEnums.ASYNC_SAVE_BATCH_ENTITY.getEventName();
            }
            GXMyBatisModelSaveBatchEntityEvent<Dict> updateEntityEvent = new GXMyBatisModelSaveBatchEntityEvent<>(source, eventType, eventParam, eventName);
            GXEventPublisherUtils.publishEvent(updateEntityEvent);
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
