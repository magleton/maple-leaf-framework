package cn.maple.core.datasource.listener;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.TypeUtil;
import cn.maple.core.datasource.event.GXMyBatisModelDeleteSoftEvent;
import cn.maple.core.datasource.event.GXMyBatisModelSaveEntityEvent;
import cn.maple.core.datasource.event.GXMyBatisModelUpdateEntityEvent;
import cn.maple.core.datasource.event.GXMyBatisModelUpdateFieldEvent;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
@Log4j2
@SuppressWarnings("all")
public class GXMyBatisListener {
    /**
     * 监听保存实体(Entity)事件
     *
     * @param saveEntityEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).SAVE_ENTITY.eventType)")
    public void listenerSaveEntity(GXMyBatisModelSaveEntityEvent<Dict> saveEntityEvent) {
        Dict source = saveEntityEvent.getSource();
        Dict param = saveEntityEvent.getParam();
        String listenerClazzName = CharSequenceUtil.lowerFirst(param.getStr("listenerClazzName"));
        Class<?> listenerClazz = Convert.convert(new TypeReference<>() {
        }, param.getObj("listenerClazz"));
        Type targetParamType = TypeUtil.getTypeArgument(listenerClazz.getGenericInterfaces()[0]);
        Object targetParamObject = Convert.convert(targetParamType, source);
        Object bean = GXSpringContextUtils.getBean(listenerClazzName, listenerClazz);
        GXCommonUtils.reflectCallObjectMethod(bean, "saveEntityListener", targetParamObject);
    }

    /**
     * 监听更新实体(Entity)事件
     *
     * @param updateEntityEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).UPDATE_ENTITY.eventType)")
    public void listenerUpdateEntity(GXMyBatisModelUpdateEntityEvent<Dict> updateEntityEvent) {
        Dict source = updateEntityEvent.getSource();
        Dict param = updateEntityEvent.getParam();
        String listenerClazzName = CharSequenceUtil.lowerFirst(param.getStr("listenerClazzName"));
        Class<?> listenerClazz = Convert.convert(new TypeReference<>() {
        }, param.getObj("listenerClazz"));
        Type targetParamType = TypeUtil.getTypeArgument(listenerClazz.getGenericInterfaces()[0]);
        Object targetParamObject = Convert.convert(targetParamType, source);
        Object bean = GXSpringContextUtils.getBean(listenerClazzName, listenerClazz);
        GXCommonUtils.reflectCallObjectMethod(bean, "updateEntityListener", targetParamObject);
    }

    /**
     * 监听更新指定字段事件
     *
     * @param updateFieldEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).UPDATE_FIELD.eventType)")
    public void listenerUpdateField(GXMyBatisModelUpdateFieldEvent<Dict> updateFieldEvent) {
        Dict source = updateFieldEvent.getSource();
        Dict param = updateFieldEvent.getParam();
        String listenerClazzName = CharSequenceUtil.lowerFirst(param.getStr("listenerClazzName"));
        Class<?> listenerClazz = Convert.convert(new TypeReference<>() {
        }, param.getObj("listenerClazz"));
        Object bean = GXSpringContextUtils.getBean(listenerClazzName, listenerClazz);
        GXCommonUtils.reflectCallObjectMethod(bean, "updateFieldListener", source);
    }

    /**
     * 监听更新指定字段事件
     *
     * @param deleteSoftEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).DELETE_SOFT.eventType)")
    public void listenerDeleteSoft(GXMyBatisModelDeleteSoftEvent<Dict> deleteSoftEvent) {
        Dict source = deleteSoftEvent.getSource();
        Dict param = deleteSoftEvent.getParam();
        String listenerClazzName = CharSequenceUtil.lowerFirst(param.getStr("listenerClazzName"));
        Class<?> listenerClazz = Convert.convert(new TypeReference<>() {
        }, param.getObj("listenerClazz"));
        Object bean = GXSpringContextUtils.getBean(listenerClazzName, listenerClazz);
        GXCommonUtils.reflectCallObjectMethod(bean, "deleteSoftListener", source);
    }
}
