package com.geoxus.core.framework.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.event.GXBaseEvent;
import com.geoxus.core.common.mapper.GXBaseMapper;
import org.springframework.cache.Cache;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 业务基础Service
 *
 * @param <T>
 * @author britton chen <britton@126.com>
 */
public interface GXBaseService<T, M extends GXBaseMapper<T>, D> {
    /**
     * 获取BaseMapper对象
     *
     * @return M
     */
    M getBaseMapper();

    /**
     * 获取BaseDao对象
     *
     * @return D
     */
    D getBaseDao();

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
     *     getSingleJSONFieldValueByDB(
     *      GoodsEntity,
     *      "ext.name",
     *      Integer.class,
     *      Dict.create().set("user_id" , 1111)
     *      )
     *     }
     * </pre>
     *
     * @param clazz     Class对象
     * @param path      路径
     * @param condition 条件
     * @return R
     */
    <R> R getSingleFieldValueByDB(Class<T> clazz, String path, Class<R> type, Dict condition);

    /**
     * 获取实体中指定指定的值
     *
     * @param clazz        Class对象
     * @param path         路径
     * @param condition    条件
     * @param defaultValue 默认值
     * @return R
     */
    <R> R getSingleFieldValueByDB(Class<T> clazz, String path, Class<R> type, Dict condition, R defaultValue);

    /**
     * 获取JSON中的多个值
     *
     * @param clazz     Class 对象
     * @param fields    字段
     * @param condition 条件
     * @return Dict
     */
    Dict getMultiFieldsValueByDB(Class<T> clazz, Dict fields, Dict condition);

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
    <R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, int coreModelId);

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
    <R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, R defaultValue, int coreModelId);

    /**
     * 获取实体的多个JSON值
     *
     * @param entity 实体对象
     * @param dict   需要获取的数据
     * @return Dict
     */
    Dict getMultiFieldsValueByEntity(T entity, Dict dict, int coreModelId);

    /**
     * 更新JSON字段中的某一个值
     *
     * @param clazz     Class对象
     * @param path      路径
     * @param value     值
     * @param condition 条件
     * @return boolean
     */
    boolean updateSingleField(Class<T> clazz, String path, Object value, Dict condition);

    /**
     * 更新JSON字段中的某多个值
     *
     * @param clazz     Class对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateMultiFields(Class<T> clazz, Dict data, Dict condition);

    /**
     * 获取Spring Cache对象
     *
     * @param cacheName 缓存名字
     * @return Cache
     */
    Cache getSpringCache(String cacheName);

    /**
     * 根据条件获取一条记录
     *
     * @param clazz     Class对象
     * @param fieldSet  字段集合
     * @param condition 查询条件
     * @param remove    是否移除
     * @return Dict
     */
    Dict getOneByCondition(Class<T> clazz, Set<String> fieldSet, Dict condition, boolean remove);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    Integer checkRecordIsExists(Class<T> clazz, Dict condition);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    Integer checkRecordIsExists(String tableName, Dict condition);

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    Integer checkRecordIsUnique(Class<T> clazz, Dict condition);

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    Integer checkRecordIsUnique(String tableName, Dict condition);

    /**
     * 获取实体的表明
     *
     * @param clazz Class对象
     * @return String
     */
    String getTableName(Class<T> clazz);

    /**
     * 修改状态
     *
     * @param status    状态
     * @param condition 条件
     * @return boolean
     */
    boolean modifyStatus(int status, Dict condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    boolean updateFieldByCondition(Class<T> clazz, Dict data, Dict condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param status    状态
     * @param condition 更新条件
     * @return boolean
     */
    boolean updateStatusByCondition(Class<T> clazz, int status, Dict condition);

    /**
     * 通过SQL语句批量插入数据
     *
     * @param clazz    实体的Class
     * @param fieldSet 字段集合
     * @param dataList 数据集合
     * @return int
     */
    Integer batchInsertBySQL(Class<T> clazz, Set<String> fieldSet, List<Dict> dataList);

    /**
     * 获取表中的指定字段
     *
     * @param clazz     Class对象
     * @param fieldSet  字段集合
     * @param condition 查询条件
     * @return Dict
     */
    Dict getFieldValueBySQL(Class<T> clazz, Set<String> fieldSet, Dict condition, boolean remove);

    /**
     * 获取表中的指定字段
     *
     * @param tableName 表名
     * @param fieldSet  字段集合
     * @param condition 更新条件
     * @param remove    是否移除
     * @return Dict
     */
    Dict getFieldValueBySQL(String tableName, Set<String> fieldSet, Dict condition, boolean remove);

    /**
     * 处理相同前缀的Dict
     *
     * @param dict 要处理的Dict
     * @return Dict
     */
    Dict handleSamePrefixDict(Dict dict);

    /**
     * 记录原表的数据到历史表里面
     *
     * @param originTableName  原表名
     * @param historyTableName 历史表名字
     * @param condition        条件
     * @param appendData       附加信息
     * @return boolean
     */
    boolean recordModificationHistory(String originTableName, String historyTableName, Dict condition, Dict appendData);

    /**
     * 通过表明获取模型ID
     *
     * @param clazz 实体的Class
     * @return int
     */
    int getCoreModelIdByTableName(Class<T> clazz);

    /**
     * 派发事件 (异步事件可以通过在监听器上面添加@Async注解实现)
     *
     * @param event ApplicationEvent对象
     */
    <R> void publishEvent(GXBaseEvent<R> event);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKey();
}
