package com.geoxus.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geoxus.common.constant.GXCommonConstant;
import com.geoxus.common.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.common.event.GXBaseEvent;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.common.pojo.GXBusinessStatusCode;
import com.geoxus.common.util.GXBaseCommonUtil;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.core.constant.GXBaseBuilderConstant;
import com.geoxus.core.dao.GXBaseDao;
import com.geoxus.core.mapper.GXBaseMapper;
import com.geoxus.core.service.GXAlterTableService;
import com.geoxus.core.service.GXDBBaseService;
import com.geoxus.core.service.GXDBSchemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务基础Service
 *
 * @param <T>
 * @author britton chen <britton@126.com>
 */
public class GXDBBaseServiceImpl<T, M extends GXBaseMapper<T>, D extends GXBaseDao<M, T>> implements GXDBBaseService<T, M, D> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = GXBaseCommonUtil.getLogger(GXDBBaseServiceImpl.class);

    /**
     * 基础DAO
     */
    @Autowired
    @SuppressWarnings("all")
    protected D baseDao;

    /**
     * 基础mapper
     */
    @Autowired
    @SuppressWarnings("all")
    protected M baseMapper;

    /**
     * 获取BaseMapper对象
     *
     * @return M
     */
    @Override
    public M getBaseMapper() {
        return baseDao.getBaseMapper();
    }

    /**
     * 获取BaseDao对象
     *
     * @return D
     */
    @Override
    public D getBaseDao() {
        return baseDao;
    }

    /**
     * 获取当前接口的常量字段信息
     *
     * @return Dict
     */
    @SuppressWarnings("unused")
    @Override
    public Dict getConstantsFields() {
        final Dict data = Dict.create();
        final List<Class<?>> clazzInterfaces = new ArrayList<>();
        GXBaseCommonUtil.getInterfaces(getClass(), clazzInterfaces);
        for (Class<?> clz : clazzInterfaces) {
            GXBaseCommonUtil.clazzFields(clz, data);
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
    @Override
    public <R> R getSingleFieldValueByDB(Class<T> clazz, String path, Class<R> type, Dict condition) {
        return getSingleFieldValueByDB(clazz, path, type, condition, GXBaseCommonUtil.getClassDefaultValue(type));
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
    @Override
    public <R> R getSingleFieldValueByDB(Class<T> clazz, String path, Class<R> type, Dict condition, R defaultValue) {
        String fieldSeparator = "::";
        Object removeObject = condition.remove("remove");
        boolean remove = null != removeObject;
        if (CharSequenceUtil.contains(path, fieldSeparator)) {
            String[] fields = CharSequenceUtil.splitToArray(path, fieldSeparator);
            path = CharSequenceUtil.format("{}{}{}", fields[0].replace("'", ""), fieldSeparator, fields[1].replace("'", ""));
        }
        final Dict dict = baseMapper.getFieldValueBySql(getTableName(clazz), CollUtil.newHashSet(path), condition, remove);
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
    @Override
    public Dict getMultiFieldsValueByDB(Class<T> clazz, Dict fields, Dict condition) {
        String fieldSeparator = "::";
        final Set<String> fieldSet = CollUtil.newHashSet();
        final Dict dataKey = Dict.create();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String key = entry.getKey();
            String aliasName = key;
            if (CharSequenceUtil.contains(key, fieldSeparator)) {
                String[] keys = CharSequenceUtil.splitToArray(key, fieldSeparator);
                aliasName = CharSequenceUtil.format("{}{}{}",
                        keys[0].replace("'", ""),
                        fieldSeparator,
                        keys[1].replace("'", ""));
                fieldSet.add(CharSequenceUtil.format("{}", aliasName));
            } else {
                fieldSet.add(CharSequenceUtil.format("{}", key));
            }
            dataKey.set(aliasName, key);
        }
        final Dict dict = baseMapper.getFieldValueBySql(getTableName(clazz), fieldSet, condition, false);
        dict.remove(GXCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME);
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
    @Override
    public <R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, int coreModelId) {
        return getSingleFieldValueByEntity(entity, path, type, GXBaseCommonUtil.getClassDefaultValue(type), coreModelId);
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
    @Override
    public <R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, R defaultValue, int coreModelId) {
        JSON json = JSONUtil.parse(JSONUtil.toJsonStr(entity));
        int index = CharSequenceUtil.indexOfIgnoreCase(path, "::");
        if (index == -1) {
            if (null == json.getByPath(path)) {
                return defaultValue;
            }
            if (JSONUtil.isJson(json.getByPath(path).toString())) {
                Dict data = Dict.create();
                Dict dict = JSONUtil.toBean(json.getByPath(path).toString(), Dict.class);
                if (!dict.isEmpty()) {
                    for (Map.Entry<String, Object> entry : dict.entrySet()) {
                        data.set(entry.getKey(), entry.getValue());
                    }
                }
                return Convert.convert(type, data);
            }
            return Convert.convert(type, json.getByPath(path));
        }
        String mainField = CharSequenceUtil.sub(path, 0, index);
        if (null == json.getByPath(mainField)) {
            throw new GXBusinessException(CharSequenceUtil.format("实体的主字段{}不存在!", mainField));
        }
        String subField = CharSequenceUtil.sub(path, index + 2, path.length());

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
    @Override
    public Dict getMultiFieldsValueByEntity(T entity, Dict dict, int coreModelId) {
        final Set<String> keySet = dict.keySet();
        final Dict data = Dict.create();
        for (String key : keySet) {
            final Object value = getSingleFieldValueByEntity(entity, key, (Class<?>) dict.getObj(key), coreModelId);
            if (Objects.isNull(value)) {
                continue;
            }
            final String[] strings = CharSequenceUtil.splitToArray(key, "::");
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSingleField(Class<T> clazz, String path, Object value, Dict condition) {
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
    @Override
    public boolean updateMultiFields(Class<T> clazz, Dict data, Dict condition) {
        return baseMapper.updateFieldByCondition(getTableName(clazz), data, condition);
    }

    /**
     * 获取Spring Cache对象
     *
     * @param cacheName 缓存名字
     * @return Cache
     */
    @Override
    public Cache getSpringCache(String cacheName) {
        return GXBaseCommonUtil.getSpringCache(cacheName);
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
    @Override
    public Dict getOneByCondition(Class<T> clazz, Set<String> fieldSet, Dict condition, boolean remove) {
        return getFieldValueBySQL(clazz, fieldSet, condition, remove);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsExists(Class<T> clazz, Dict condition) {
        return baseMapper.checkRecordIsExists(getTableName(clazz), condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsExists(String tableName, Dict condition) {
        return baseMapper.checkRecordIsExists(tableName, condition);
    }

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsUnique(Class<T> clazz, Dict condition) {
        return baseMapper.checkRecordIsUnique(getTableName(clazz), condition);
    }

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsUnique(String tableName, Dict condition) {
        return baseMapper.checkRecordIsUnique(tableName, condition);
    }

    /**
     * 获取实体的表明
     *
     * @param clazz Class对象
     * @return String
     */
    @Override
    public String getTableName(Class<T> clazz) {
        return GXBaseCommonUtil.getTableName(clazz);
    }

    /**
     * 修改状态
     *
     * @param status    状态
     * @param condition 条件
     * @return boolean
     */
    @Override
    public boolean modifyStatus(int status, Dict condition) {
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
    @Override
    public boolean updateFieldByCondition(Class<T> clazz, Dict data, Dict condition) {
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
    @Override
    public boolean updateStatusByCondition(Class<T> clazz, int status, Dict condition) {
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
    @SuppressWarnings("all")
    @Override
    public Integer batchInsertBySQL(Class<T> clazz, Set<String> fieldSet, List<Dict> dataList) {
        return baseMapper.batchInsertBySql(getTableName(clazz), fieldSet, dataList);
    }

    /**
     * 获取表中的指定字段
     *
     * @param clazz     Class对象
     * @param fieldSet  字段集合
     * @param condition 查询条件
     * @return Dict
     */
    @Override
    public Dict getFieldValueBySQL(Class<T> clazz, Set<String> fieldSet, Dict condition, boolean remove) {
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
    @Override
    public Dict getFieldValueBySQL(String tableName, Set<String> fieldSet, Dict condition, boolean remove) {
        final Dict dict = baseMapper.getFieldValueBySql(tableName, fieldSet, condition, remove);
        if (Objects.isNull(dict)) {
            GXBaseCommonUtil.getLogger(GXDBBaseServiceImpl.class).error("在{}获取{}字段的数据不存在...", tableName, fieldSet);
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
    @Override
    public Dict handleSamePrefixDict(Dict dict) {
        String fieldSeparator = "::";
        final Set<Map.Entry<String, Object>> entries = dict.entrySet();
        final Dict retDict = Dict.create();
        for (Map.Entry<String, Object> entry : entries) {
            final String key = entry.getKey();
            final Object object = entry.getValue();
            if (CharSequenceUtil.contains(key, fieldSeparator)) {
                String[] keys = CharSequenceUtil.splitToArray(key, fieldSeparator);
                Dict data = Convert.convert(Dict.class, Optional.ofNullable(retDict.getObj(keys[0])).orElse(Dict.create()));
                retDict.set(CharSequenceUtil.toCamelCase(keys[0]), data.set(CharSequenceUtil.toCamelCase(keys[1]), object));
            } else {
                retDict.set(CharSequenceUtil.toCamelCase(key), object);
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
    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean recordModificationHistory(String originTableName, String historyTableName, Dict condition, Dict appendData) {
        final Dict targetDict = baseMapper.getFieldValueBySql(originTableName, CollUtil.newHashSet("*"), condition, true);
        if (targetDict.isEmpty()) {
            return false;
        }
        GXAlterTableService gxAlterTableService = GXSpringContextUtil.getBean(GXAlterTableService.class);
        assert gxAlterTableService != null;
        List<GXDBSchemaService.TableField> tableColumns = CollUtil.newArrayList();
        try {
            tableColumns = gxAlterTableService.getTableColumns(historyTableName);
        } catch (SQLException e) {
            LoggerFactory.getLogger(GXDBBaseServiceImpl.class).error(e.getMessage(), e);
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
        tableValues.set("updated_at", tableValues.getLong("created_at"));
        tableValues.set("created_at", DateUtil.current());
        final Integer insert = baseMapper.batchInsertBySql(historyTableName, lastTableField, CollUtil.newArrayList(tableValues));
        return insert > 0;
    }

    /**
     * 通过表明获取模型ID
     *
     * @param clazz 实体的Class
     * @return int
     */
    @Override
    public int getCoreModelIdByTableName(Class<T> clazz) {
        String tableName = getTableName(clazz);
        return getSingleFieldValueByDB(clazz, "model_id", Integer.class, Dict.create().set("table_name", tableName));
    }

    /**
     * 派发事件 (异步事件可以通过在监听器上面添加@Async注解实现)
     *
     * @param event ApplicationEvent对象
     */
    @Override
    public <R> void publishEvent(GXBaseEvent<R> event) {
        GXBaseCommonUtil.publishEvent(event);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    @Override
    public <R> GXPaginationProtocol<R> listOrSearchPage(GXBaseSearchReqProtocol searchReqDto) {
        final Dict param = Dict.create();
        if (Objects.nonNull(searchReqDto.getPagingInfo())) {
            param.set("pagingInfo", searchReqDto.getPagingInfo());
        }
        if (Objects.nonNull(searchReqDto.getSearchCondition())) {
            param.set(GXBaseBuilderConstant.SEARCH_CONDITION_NAME, searchReqDto.getSearchCondition());
        }
        return generatePage(param);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param param 参数
     * @return GXPagination
     */
    @Override
    public <R> GXPaginationProtocol<R> listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    /**
     * 内容详情
     *
     * @param param 参数
     * @return Dict
     */
    @Override
    public Dict detail(Dict param) {
        final String tableName = (String) param.remove("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException("请提供表名!");
        }
        final String fields = (String) Optional.ofNullable(param.remove("fields")).orElse("*");
        final boolean remove = (boolean) Optional.ofNullable(param.remove("remove")).orElse(false);
        Set<String> lastFields = Arrays.stream(CharSequenceUtil.replace(fields, " ", "").split(",")).collect(Collectors.toSet());
        Dict condition = Convert.convert(Dict.class, Optional.ofNullable(param.getObj(GXBaseBuilderConstant.SEARCH_CONDITION_NAME)).orElse(Dict.create()));
        return getFieldValueBySQL(tableName, lastFields, condition, remove);
    }

    /**
     * 批量更新status字段
     *
     * @param param 参数
     * @return boolean
     */
    @Override
    public boolean batchUpdateStatus(Dict param) {
        final List<Long> ids = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj(getPrimaryKey()));
        if (null == ids) {
            return false;
        }
        final List<T> updateEntities = new ArrayList<>();
        for (Long id : ids) {
            T entity = baseDao.getById(id);
            final Object status = ReflectUtil.invoke(entity, "getStatus");
            Long entityCurrentStatus = 0L;
            if (null != status) {
                entityCurrentStatus = Convert.toLong(status, 0L);
            }
            if ((entityCurrentStatus & GXBusinessStatusCode.DELETED.getCode()) != GXBusinessStatusCode.DELETED.getCode()) {
                ReflectUtil.invoke(entity, "setStatus", GXBusinessStatusCode.DELETED.getCode());
                updateEntities.add(entity);
            }
        }
        return updateEntities.isEmpty() || baseDao.updateBatchById(updateEntities);
    }

    /**
     * 通过条件获取配置信息
     *
     * @param condition 条件
     * @return Dict
     */
    @Override
    public Dict getDataByCondition(Dict condition, String... fields) {
        Dict retData = Dict.create();
        final T entity = baseDao.getOne(new QueryWrapper<T>().select(fields).allEq(condition));
        if (Objects.isNull(entity)) {
            return retData;
        }
        Dict dict = Dict.parse(entity);
        Arrays.stream(fields).forEach(key -> {
            Object value = dict.getObj(key);
            if (Objects.nonNull(value) && JSONUtil.isJson(value.toString())) {
                value = JSONUtil.toBean(value.toString(), Dict.class);
            }
            retData.set(key, value);
        });
        return retData;
    }

    /**
     * 实现验证注解(返回true表示数据已经存在)
     *
     * @param value                      The value to check for
     * @param fieldName                  The name of the field for which to check if the value exists
     * @param constraintValidatorContext The ValidatorContext
     * @param param                      param
     * @return boolean
     */
    @Override
    public boolean validateExists(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        String tableName = param.getStr("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        return 1 == checkRecordIsExists(tableName, Dict.create().set(fieldName, value));
    }

    /**
     * 验证数据的唯一性 (返回true表示数据已经存在)
     *
     * @param value                      值
     * @param fieldName                  字段名字
     * @param constraintValidatorContext 验证上下文对象
     * @param param                      参数
     * @return boolean
     */
    @Override
    public boolean validateUnique(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) {
        String tableName = param.getStr("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        return checkRecordIsUnique(tableName, Dict.create().set(fieldName, value)) > 1;
    }

    /**
     * 获取分页对象信息
     *
     * @param param 参数
     * @return IPage
     */
    @Override
    public <R> IPage<R> constructPageObjectFromParam(Dict param) {
        final Dict pageInfo = getPageInfoFromParam(param);
        return new Page<>(pageInfo.getInt("page"), pageInfo.getInt("pageSize"));
    }

    /**
     * 从请求参数中获取分页的信息
     *
     * @param param 参数
     * @return Dict
     */
    @Override
    public Dict getPageInfoFromParam(Dict param) {
        int currentPage = GXCommonConstant.DEFAULT_CURRENT_PAGE;
        int pageSize = GXCommonConstant.DEFAULT_PAGE_SIZE;
        final Dict pagingInfo = Convert.convert(Dict.class, param.getObj("pagingInfo"));
        if (null != pagingInfo) {
            if (null != pagingInfo.getInt("page")) {
                currentPage = pagingInfo.getInt("page");
            }
            if (null != pagingInfo.getInt("pageSize")) {
                pageSize = pagingInfo.getInt("pageSize");
            }
        }
        return Dict.create().set("page", currentPage).set("pageSize", pageSize);
    }

    /**
     * 获取分页信息
     *
     * @param param 查询参数
     * @return GXPagination
     */
    @Override
    public <R> GXPaginationProtocol<R> generatePage(Dict param) {
        String mapperMethodName = "listOrSearchPage";
        return generatePage(param, mapperMethodName);
    }

    /**
     * 分页  返回实体对象
     *
     * @param param            参数
     * @param mapperMethodName Mapper方法
     * @return GXPagination
     */
    @Override
    public <R> GXPaginationProtocol<R> generatePage(Dict param, String mapperMethodName) {
        final IPage<R> riPage = constructPageObjectFromParam(param);
        Method mapperMethod = ReflectUtil.getMethodByName(baseMapper.getClass(), mapperMethodName);
        if (Objects.isNull(mapperMethod)) {
            Class<?>[] interfaces = baseMapper.getClass().getInterfaces();
            if (interfaces.length > 0) {
                String canonicalName = interfaces[0].getCanonicalName();
                throw new GXBusinessException(CharSequenceUtil.format("请在{}类中实现{}方法", canonicalName, mapperMethodName));
            }
            throw new GXBusinessException(CharSequenceUtil.format("请在相应的Mapper类中实现{}方法", mapperMethodName));
        }
        final List<R> list = ReflectUtil.invoke(baseMapper, mapperMethod, riPage, param);
        riPage.setRecords(list);
        return new GXPaginationProtocol<>(riPage.getRecords(), riPage.getTotal(), riPage.getSize(), riPage.getCurrent());
    }

    /**
     * 获取记录的父级path
     *
     * @param parentId   父级ID
     * @param appendSelf 　是否将parentId附加到返回结果上面
     * @return String
     */
    @Override
    public String getParentPath(Class<T> clazz, Long parentId, boolean appendSelf) {
        Dict condition = Dict.create().set(getPrimaryKey(), parentId);
        final Dict dict = getFieldValueBySQL(clazz, CollUtil.newHashSet("path"), condition, false);
        if (null == dict || dict.isEmpty()) {
            return "0";
        }
        if (appendSelf) {
            return CharSequenceUtil.format("{}-{}", dict.getStr("path"), parentId);
        }
        return dict.getStr("path");
    }

    /**
     * 加密手机号码
     *
     * @param phoneNumber 明文手机号
     * @return String
     */
    @Override
    public String encryptedPhoneNumber(String phoneNumber) {
        return GXBaseCommonUtil.encryptedPhoneNumber(phoneNumber);
    }

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @return String
     */
    @Override
    public String decryptedPhoneNumber(String encryptPhoneNumber) {
        return GXBaseCommonUtil.decryptedPhoneNumber(encryptPhoneNumber);
    }

    /**
     * 隐藏手机号码的指定几位为指定的字符
     *
     * @param phoneNumber  手机号码
     * @param startInclude 开始字符位置(包含,从0开始)
     * @param endExclude   结束字符位置
     * @param replacedChar 替换为的字符
     * @return String
     */
    @Override
    public String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar) {
        return GXBaseCommonUtil.hiddenPhoneNumber(phoneNumber, startInclude, endExclude, replacedChar);
    }

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    @Override
    public String getPrimaryKey() {
        return "id";
    }
}
