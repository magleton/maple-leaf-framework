package com.geoxus.core.framework.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.event.GXBaseEvent;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXSpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

/**
 * 业务基础Service
 *
 * @param <T>
 * @author britton chen <britton@126.com>
 */
public interface GXBaseService<T> extends IService<T> {
    Logger logger = GXCommonUtils.getLogger(GXBaseService.class);

    /**
     * 获取当前接口的常量字段信息
     *
     * @return Dict
     */
    @SuppressWarnings("unused")
    default Dict getConstantsFields() {
        final Dict data = Dict.create();
        final List<Class<?>> clazzInterfaces = new ArrayList<>();
        GXCommonUtils.getInterfaces(getClass(), clazzInterfaces);
        for (Class<?> clz : clazzInterfaces) {
            GXCommonUtils.clazzFields(clz, data);
        }
        return data;
    }

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
    default <R> R getSingleFieldValueByDB(Class<T> clazz, String path, Class<R> type, Dict condition) {
        return getSingleFieldValueByDB(clazz, path, type, condition, GXCommonUtils.getClassDefaultValue(type));
    }

    /**
     * 获取实体中指定指定的值
     *
     * @param clazz        Class对象
     * @param path         路径
     * @param condition    条件
     * @param defaultValue 默认值
     * @return R
     */
    default <R> R getSingleFieldValueByDB(Class<T> clazz, String path, Class<R> type, Dict condition, R defaultValue) {
        Object removeObject = condition.remove("remove");
        boolean remove = null != removeObject;
        if (StrUtil.contains(path, "::")) {
            String[] fields = StrUtil.split(path, "::");
            path = StrUtil.format("{}::{}", fields[0].replace("'", ""), fields[1].replace("'", ""));
        }
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        final Dict dict = baseMapper.getFieldValueBySQL(getTableName(clazz), CollUtil.newHashSet(path), condition, remove);
        if (null == dict) {
            return defaultValue;
        }
        Object obj = dict.get(path);
        if (null == obj) {
            return defaultValue;
        }
        if (obj instanceof byte[]) {
            obj = new String((byte[]) obj, StandardCharsets.UTF_8);
        }
        return Convert.convert(type, obj);
    }

    /**
     * 获取JSON中的多个值
     *
     * @param clazz     Class 对象
     * @param fields    字段
     * @param condition 条件
     * @return Dict
     */
    @SuppressWarnings("unused")
    default Dict getMultiFieldsValueByDB(Class<T> clazz, Dict fields, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        final Set<String> fieldSet = CollUtil.newHashSet();
        final Dict dataKey = Dict.create();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String key = entry.getKey();
            String aliasName = key;
            if (CharSequenceUtil.contains(key, "::")) {
                String[] keys = CharSequenceUtil.split(key, "::");
                aliasName = CharSequenceUtil.format("{}::{}",
                        keys[0].replace("'", ""),
                        keys[1].replace("'", ""));
                fieldSet.add(CharSequenceUtil.format("{}", aliasName));
            } else {
                fieldSet.add(CharSequenceUtil.format("{}", key));
            }
            dataKey.set(aliasName, key);
        }
        final Dict dict = baseMapper.getFieldValueBySQL(getTableName(clazz), fieldSet, condition, false);
        dict.remove(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME);
        return handleSamePrefixDict(dict);
    }

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
    default <R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, int coreModelId) {
        return getSingleFieldValueByEntity(entity, path, type, GXCommonUtils.getClassDefaultValue(type), coreModelId);
    }

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
    default <R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, R defaultValue, int coreModelId) {
        GXCoreModelAttributePermissionService permissionService = GXSpringContextUtils.getBean(GXCoreModelAttributePermissionService.class);
        assert permissionService != null;
        Dict permissions = permissionService.getModelAttributePermissionByCoreModelId(coreModelId, Dict.create());
        Dict jsonField = Convert.convert(Dict.class, permissions.getObj("json_field"));
        Dict dbField = Convert.convert(Dict.class, permissions.getObj("db_field"));
        JSON json = JSONUtil.parse(JSONUtil.toJsonStr(entity));
        int index = CharSequenceUtil.indexOfIgnoreCase(path, "::");
        if (index == -1) {
            if (null != dbField.get(path)) {
                GXCommonUtils.getLogger(GXBaseService.class).error("当前用户无权访问{}字段的数据...", path);
                return null;
            }
            if (null == json.getByPath(path)) {
                return defaultValue;
            }
            if (JSONUtil.isJson(json.getByPath(path).toString())) {
                Dict data = Dict.create();
                Dict dict = JSONUtil.toBean(json.getByPath(path).toString(), Dict.class);
                Dict obj = Convert.convert(Dict.class, Optional.ofNullable(jsonField.getObj(path)).orElse(Dict.class));
                if (!dict.isEmpty()) {
                    for (Map.Entry<String, Object> entry : dict.entrySet()) {
                        if (null == obj.get(entry.getKey())) {
                            data.set(entry.getKey(), entry.getValue());
                        }
                    }
                }
                return Convert.convert(type, data);
            }
            return Convert.convert(type, json.getByPath(path));
        }
        String mainField = CharSequenceUtil.sub(path, 0, index);
        if (null == json.getByPath(mainField)) {
            throw new GXException(CharSequenceUtil.format("实体的主字段{}不存在!", mainField));
        }
        String subField = CharSequenceUtil.sub(path, index + 2, path.length());
        Dict dict = Convert.convert(Dict.class, Optional.ofNullable(jsonField.getObj(mainField)).orElse(Dict.create()));
        if (!dict.isEmpty() && null != dict.get(subField)) {
            GXCommonUtils.getLogger(GXBaseService.class).error("当前用户无权访问{}.{}字段的数据...", mainField, subField);
            return null;
        }
        JSON parse = JSONUtil.parse(json.getByPath(mainField));
        if (null == parse) {
            return defaultValue;
        }
        return Convert.convert(type, parse.getByPath(subField), defaultValue);
    }

    /**
     * 获取实体的多个JSON值
     *
     * @param entity 实体对象
     * @param dict   需要获取的数据
     * @return Dict
     */
    default Dict getMultiFieldsValueByEntity(T entity, Dict dict, int coreModelId) {
        final Set<String> keySet = dict.keySet();
        final Dict data = Dict.create();
        for (String key : keySet) {
            final Object value = getSingleFieldValueByEntity(entity, key, (Class<?>) dict.getObj(key), coreModelId);
            if (Objects.isNull(value)) {
                continue;
            }
            final String[] strings = CharSequenceUtil.split(key, "::");
            Object o = data.get(strings[0]);
            if (strings.length > 1) {
                if (null != o) {
                    Dict convert = Convert.convert(Dict.class, o);
                    convert.set(strings[strings.length - 1], value);
                    data.set(strings[0], convert);
                } else {
                    data.set(strings[0], Dict.create().set(strings[strings.length - 1], value));
                }
            } else {
                data.set(strings[strings.length - 1], value);
            }
        }
        return data;
    }

    /**
     * 更新JSON字段中的某一个值
     *
     * @param clazz     Class对象
     * @param path      路径
     * @param value     值
     * @param condition 条件
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateSingleField(Class<T> clazz, String path, Object value, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        int index = CharSequenceUtil.indexOfIgnoreCase(path, "::");
        String mainPath = CharSequenceUtil.sub(path, 0, index);
        String subPath = CharSequenceUtil.sub(path, index + 1, path.length());
        final Dict data = Dict.create().set(mainPath, Dict.create().set(subPath, value));
        return baseMapper.updateFieldByCondition(getTableName(clazz), data, condition);
    }

    /**
     * 更新JSON字段中的某多个值
     *
     * @param clazz     Class对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateMultiFields(Class<T> clazz, Dict data, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        return baseMapper.updateFieldByCondition(getTableName(clazz), data, condition);
    }

    /**
     * 获取Spring Cache对象
     *
     * @param cacheName 缓存名字
     * @return Cache
     */
    default Cache getSpringCache(String cacheName) {
        return GXCommonUtils.getSpringCache(cacheName);
    }

    /**
     * 根据条件获取一条记录
     *
     * @param clazz     Class对象
     * @param fieldSet  字段集合
     * @param condition 查询条件
     * @param remove    是否移除
     * @return Dict
     */
    @SuppressWarnings("unused")
    default Dict getOneByCondition(Class<T> clazz, Set<String> fieldSet, Dict condition, boolean remove) {
        return getFieldValueBySQL(clazz, fieldSet, condition, remove);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    default Integer checkRecordIsExists(Class<T> clazz, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        return baseMapper.checkRecordIsExists(getTableName(clazz), condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    default Integer checkRecordIsExists(String tableName, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        return baseMapper.checkRecordIsExists(tableName, condition);
    }

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    default Integer checkRecordIsUnique(Class<T> clazz, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        return baseMapper.checkRecordIsUnique(getTableName(clazz), condition);
    }

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    default Integer checkRecordIsUnique(String tableName, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        return baseMapper.checkRecordIsUnique(tableName, condition);
    }

    /**
     * 获取实体的表明
     *
     * @param clazz Class对象
     * @return String
     */
    default String getTableName(Class<T> clazz) {
        return GXCommonUtils.getTableName(clazz);
    }

    /**
     * 修改状态
     *
     * @param status    状态
     * @param condition 条件
     * @return boolean
     */
    default boolean modifyStatus(int status, Dict condition) {
        final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Class<T> clazz = Convert.convert(new TypeReference<Class<T>>() {
        }, type);
        return updateStatusByCondition(clazz, status, condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    default boolean updateFieldByCondition(Class<T> clazz, Dict data, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        return baseMapper.updateFieldByCondition(getTableName(clazz), data, condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param status    状态
     * @param condition 更新条件
     * @return boolean
     */
    default boolean updateStatusByCondition(Class<T> clazz, int status, Dict condition) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        return baseMapper.updateStatusByCondition(getTableName(clazz), status, condition);
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param clazz    实体的Class
     * @param fieldSet 字段集合
     * @param dataList 数据集合
     * @return int
     */
    @SuppressWarnings("unused")
    default Integer batchInsertBySQL(Class<T> clazz, Set<String> fieldSet, List<Dict> dataList) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        return baseMapper.batchInsertBySQL(getTableName(clazz), fieldSet, dataList);
    }

    /**
     * 获取表中的指定字段
     *
     * @param clazz     Class对象
     * @param fieldSet  字段集合
     * @param condition 查询条件
     * @return Dict
     */
    default Dict getFieldValueBySQL(Class<T> clazz, Set<String> fieldSet, Dict condition, boolean remove) {
        final String tableName = getTableName(clazz);
        return getFieldValueBySQL(tableName, fieldSet, condition, remove);
    }

    /**
     * 获取表中的指定字段
     *
     * @param tableName 表名
     * @param fieldSet  字段集合
     * @param condition 更新条件
     * @param remove    是否移除
     * @return Dict
     */
    default Dict getFieldValueBySQL(String tableName, Set<String> fieldSet, Dict condition, boolean remove) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        final Dict dict = baseMapper.getFieldValueBySQL(tableName, fieldSet, condition, remove);
        if (null == dict) {
            GXCommonUtils.getLogger(GXBaseService.class).error("在{}获取{}字段的数据不存在...", tableName, fieldSet);
            return Dict.create();
        }
        return handleSamePrefixDict(dict);
    }

    /**
     * 处理相同前缀的Dict
     *
     * @param dict 要处理的Dict
     * @return Dict
     */
    default Dict handleSamePrefixDict(Dict dict) {
        final Set<Map.Entry<String, Object>> entries = dict.entrySet();
        final Dict retDict = Dict.create();
        for (Map.Entry<String, Object> entry : entries) {
            final String key = entry.getKey();
            final Object object = entry.getValue();
            if (CharSequenceUtil.contains(key, "::")) {
                String[] keys = CharSequenceUtil.splitToArray(key, "::");
                Dict data = Convert.convert(Dict.class, Optional.ofNullable(retDict.getObj(keys[0])).orElse(Dict.create()));
                retDict.set(keys[0], data.set(keys[1], object));
            } else {
                retDict.set(key, object);
            }
        }
        return retDict;
    }

    /**
     * 记录原表的数据到历史表里面
     *
     * @param originTableName  原表名
     * @param historyTableName 历史表名字
     * @param condition        条件
     * @param appendData       附加信息
     * @return boolean
     */
    @SuppressWarnings("unused")
    @Transactional(rollbackFor = Exception.class)
    default boolean recordModificationHistory(String originTableName, String historyTableName, Dict condition, Dict appendData) {
        GXBaseMapper<T> baseMapper = (GXBaseMapper<T>) getBaseMapper();
        assert baseMapper != null;
        final Dict targetDict = baseMapper.getFieldValueBySQL(originTableName, CollUtil.newHashSet("*"), condition, true);
        if (targetDict.isEmpty()) {
            return false;
        }
        GXAlterTableService gxAlterTableService = GXSpringContextUtils.getBean(GXAlterTableService.class);
        assert gxAlterTableService != null;
        List<GXDBSchemaService.TableField> tableColumns = CollUtil.newArrayList();
        try {
            tableColumns = gxAlterTableService.getTableColumns(historyTableName);
        } catch (SQLException e) {
            LoggerFactory.getLogger(GXBaseService.class).error(e.getMessage(), e);
        }
        if (tableColumns.isEmpty()) {
            return false;
        }
        final Set<String> tableField = CollUtil.newHashSet();
        for (GXDBSchemaService.TableField field : tableColumns) {
            tableField.add(field.getColumnName());
        }
        final Dict tableValues = Dict.create();
        final HashSet<String> lastTableField = new HashSet<>();
        for (String key : tableField) {
            String value = targetDict.getStr(key);
            if (null != value) {
                lastTableField.add(key);
                final Object dataObj = appendData.getObj(key);
                if (null != dataObj) {
                    if (JSONUtil.isJson(value)) {
                        final Dict toBean = JSONUtil.toBean(value, Dict.class);
                        if ((dataObj instanceof Dict)) {
                            toBean.putAll((Dict) dataObj);
                        } else {
                            toBean.set(key, dataObj);
                        }
                        value = JSONUtil.toJsonStr(toBean);
                    } else {
                        value = value.concat("::" + dataObj);
                    }
                }
                tableValues.set(key, value);
            }
        }
        tableValues.set("updated_at", tableValues.getInt("created_at"));
        tableValues.set("created_at", DateUtil.currentSeconds());
        final Integer insert = baseMapper.batchInsertBySQL(historyTableName, lastTableField, CollUtil.newArrayList(tableValues));
        return insert > 0;
    }

    /**
     * 通过表明获取模型ID
     *
     * @param clazz 实体的Class
     * @return int
     */
    default int getCoreModelIdByTableName(Class<T> clazz) {
        String tableName = getTableName(clazz);
        return getSingleFieldValueByDB(clazz, "model_id", Integer.class, Dict.create().set("table_name", tableName));
    }

    /**
     * 派发事件 (异步事件可以通过在监听器上面添加@Async注解实现)
     *
     * @param event ApplicationEvent对象
     */
    default <R> void publishEvent(GXBaseEvent<R> event) {
        GXCommonUtils.publishEvent(event);
    }

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    default String getPrimaryKey() {
        return "id";
    }
}
