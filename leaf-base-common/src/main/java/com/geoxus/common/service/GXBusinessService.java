package com.geoxus.common.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.event.GXBaseEvent;
import org.springframework.cache.Cache;

public interface GXBusinessService {
    /**
     * 加密手机号码
     *
     * @param phoneNumber 明文手机号
     * @return String
     */
    String encryptedPhoneNumber(String phoneNumber);

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @return String
     */
    String decryptedPhoneNumber(String encryptPhoneNumber);

    /**
     * 隐藏手机号码的指定几位为指定的字符
     *
     * @param phoneNumber  手机号码
     * @param startInclude 开始字符位置(包含,从0开始)
     * @param endExclude   结束字符位置
     * @param replacedChar 替换为的字符
     * @return String
     */
    String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar);

    /**
     * 从请求参数中获取分页的信息
     *
     * @param param 参数
     * @return Dict
     */
    Dict getPageInfoFromParam(Dict param);

    /**
     * 派发事件 (异步事件可以通过在监听器上面添加@Async注解实现)
     *
     * @param event ApplicationEvent对象
     */
    <R> void publishEvent(GXBaseEvent<R> event);

    /**
     * 获取当前接口的常量字段信息
     *
     * @return Dict
     */
    Dict getConstantsFields();


    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       )
     *     }
     * </pre>
     *
     * @param entity 实体对象
     * @param path   路径
     * @return R
     */
    <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, int coreModelId);

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       0
     *       )
     *     }
     * </pre>
     *
     * @param entity       实体对象
     * @param path         路径
     * @param defaultValue 默认值
     * @return R
     */
    <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, R defaultValue, int coreModelId);

    /**
     * 获取实体的多个JSON值
     *
     * @param entity 实体对象
     * @param dict   需要获取的数据
     * @return Dict
     */
    <T> Dict getMultiFieldsValueByEntity(T entity, Dict dict, int coreModelId);

    /**
     * 获取Spring Cache对象
     *
     * @param cacheName 缓存名字
     * @return Cache
     */
    Cache getSpringCache(String cacheName);

    /**
     * 获取实体的表明
     *
     * @param clazz Class对象
     * @return String
     */
    <T> String getTableName(Class<T> clazz);
    
    /**
     * 处理相同前缀的Dict
     *
     * @param dict 要处理的Dict
     * @return Dict
     */
    Dict handleSamePrefixDict(Dict dict);
}
