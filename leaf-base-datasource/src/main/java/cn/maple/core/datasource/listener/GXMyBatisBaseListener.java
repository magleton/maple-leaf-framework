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

import java.lang.reflect.Type;

@SuppressWarnings("all")
interface GXMyBatisBaseListener {
    /**
     * 监听保存实体(Entity)事件
     *
     * @param saveEntityEvent 事件对象
     */
    default void listenerSaveEntity(GXMyBatisModelSaveEntityEvent<Dict> saveEntityEvent) {
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
    default void listenerUpdateEntity(GXMyBatisModelUpdateEntityEvent<Dict> updateEntityEvent) {
        Dict source = updateEntityEvent.getSource();
        Dict param = updateEntityEvent.getParam();
        String listenerClazzName = CharSequenceUtil.lowerFirst(param.getStr("listenerClazzName"));
        Class<?> listenerClazz = Convert.convert(new TypeReference<>() {
        }, param.getObj("listenerClazz"));
        Type targetParamType = TypeUtil.getTypeArgument(listenerClazz.getGenericInterfaces()[0]);
        Object targetParamObject = Convert.convert(targetParamType, source.get("entityData"));
        Object keyOperatorPairs = source.get("keyOperatorPairs");
        Object keyValuePairs = source.get("keyValuePairs");
        Object bean = GXSpringContextUtils.getBean(listenerClazzName, listenerClazz);
        GXCommonUtils.reflectCallObjectMethod(bean, "updateEntityListener", targetParamObject, keyValuePairs, keyOperatorPairs);
    }

    /**
     * 监听更新指定字段事件
     *
     * @param updateFieldEvent 事件对象
     */
    default void listenerUpdateField(GXMyBatisModelUpdateFieldEvent<Dict> updateFieldEvent) {
        Dict source = updateFieldEvent.getSource();
        Dict param = updateFieldEvent.getParam();
        String listenerClazzName = CharSequenceUtil.lowerFirst(param.getStr("listenerClazzName"));
        Class<?> listenerClazz = Convert.convert(new TypeReference<>() {
        }, param.getObj("listenerClazz"));
        Object bean = GXSpringContextUtils.getBean(listenerClazzName, listenerClazz);
        Dict updateFieldData = Convert.convert(Dict.class, source.getObj("updateFieldData"));
        Dict conditionFieldData = Convert.convert(Dict.class, source.get("conditionFieldData"));
        GXCommonUtils.reflectCallObjectMethod(bean, "updateFieldListener", updateFieldData, conditionFieldData);
    }

    /**
     * 监听更新指定字段事件
     *
     * @param deleteSoftEvent 事件对象
     */
    default void listenerDeleteSoft(GXMyBatisModelDeleteSoftEvent<Dict> deleteSoftEvent) {
        Dict source = deleteSoftEvent.getSource();
        Dict param = deleteSoftEvent.getParam();
        String listenerClazzName = CharSequenceUtil.lowerFirst(param.getStr("listenerClazzName"));
        Class<?> listenerClazz = Convert.convert(new TypeReference<>() {
        }, param.getObj("listenerClazz"));
        Object bean = GXSpringContextUtils.getBean(listenerClazzName, listenerClazz);
        GXCommonUtils.reflectCallObjectMethod(bean, "deleteSoftListener", source);
    }
}
