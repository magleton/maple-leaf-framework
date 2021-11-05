package cn.maple.core.datasource.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.service.GXAlterTableService;
import cn.maple.core.datasource.service.GXDBSchemaService;
import cn.maple.core.datasource.service.impl.GXDBBaseServiceImpl;
import cn.maple.core.datasource.util.GXDataSourceCommonUtils;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class GXBaseDao<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, R extends GXBaseResDto> extends ServiceImpl<M, T> {
    /**
     * 分页  返回实体对象
     *
     * @param param            参数
     * @param mapperMethodName Mapper方法
     * @return GXPagination
     */
    public IPage<R> generatePage(IPage<R> riPage, Dict param, String mapperMethodName) {
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
        return riPage;
    }

    /**
     * 获取实体中指定指定的值
     *
     * @param clazz        Class对象
     * @param path         路径
     * @param condition    条件
     * @param defaultValue 默认值
     * @return E
     */
    public <E> E getSingleFieldValueByDB(Class<T> clazz, String path, Class<E> type, Dict condition, E defaultValue) {
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
        return baseMapper.getFieldValueBySql(getTableName(clazz), fieldSet, condition, false);
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
    public boolean updateMultiFields(Class<T> clazz, Dict data, Dict condition) {
        return baseMapper.updateFieldByCondition(getTableName(clazz), data, condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
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
    public Integer checkRecordIsUnique(String tableName, Dict condition) {
        return baseMapper.checkRecordIsUnique(tableName, condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
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
    public Integer batchInsertBySQL(Class<T> clazz, Set<String> fieldSet, List<Dict> dataList) {
        return baseMapper.batchInsertBySql(getTableName(clazz), fieldSet, dataList);
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
    public Dict getFieldValueBySQL(String tableName, Set<String> fieldSet, Dict condition, boolean remove) {
        final Dict dict = baseMapper.getFieldValueBySql(tableName, fieldSet, condition, remove);
        if (Objects.isNull(dict)) {
            GXCommonUtils.getLogger(GXDBBaseServiceImpl.class).error("在{}获取{}字段的数据不存在...", tableName, fieldSet);
            return Dict.create();
        }
        return dict;
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
    public boolean recordModificationHistory(String originTableName, String historyTableName, Dict condition, Dict appendData) {
        final Dict targetDict = baseMapper.getFieldValueBySql(originTableName, CollUtil.newHashSet("*"), condition, true);
        if (targetDict.isEmpty()) {
            return false;
        }
        GXAlterTableService gxAlterTableService = GXSpringContextUtils.getBean(GXAlterTableService.class);
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
     * 获取表名字
     *
     * @param clazz 实体的类型
     * @return 数据库表的名字
     */
    private String getTableName(Class<T> clazz) {
        return GXDataSourceCommonUtils.getTableName(clazz);
    }
}