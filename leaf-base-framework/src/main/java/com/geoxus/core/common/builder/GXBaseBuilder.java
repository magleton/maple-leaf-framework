package com.geoxus.core.common.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.core.framework.service.GXDBSchemaService;
import com.google.common.collect.Table;
import org.apache.ibatis.jdbc.SQL;

import java.util.*;

public interface GXBaseBuilder {
    /**
     * 更新实体字段和虚拟字段
     * <pre>
     *   {@code
     *    final Table<String, String, Object> extData = HashBasedTable.create();
     *    extData.put("ext", "name", "jack");
     *    extData.put("ext", "address", "四川成都");
     *    extData.put("ext", "-salary" , "")
     *    final Dict data = Dict.create().set("category_name", "打折商品").set("ext", extData);
     *    updateFieldByCondition("s_category", data, Dict.create().set("category_id", 2));
     *  }
     *  </pre>
     *
     * @param tableName 表名
     * @param data      数据
     * @param whereData 条件
     * @return String
     */
    @SuppressWarnings("unused")
    static String updateFieldByCondition(String tableName, Dict data, Dict whereData) {
        final SQL sql = new SQL().UPDATE(tableName);
        final Set<String> dataKeys = data.keySet();
        for (String dataKey : dataKeys) {
            Object value = data.getObj(dataKey);
            if (value instanceof Table) {
                Table<String, String, Object> table = Convert.convert(new TypeReference<Table<String, String, Object>>() {
                }, value);
                final Map<String, Object> row = table.row(dataKey);
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    final String entryKey = entry.getKey();
                    Object entryValue = entry.getValue();
                    if (entryKey.startsWith("-")) {
                        sql.SET(StrUtil.format("{} = JSON_REMOVE({} , '$.{}')", dataKey, dataKey, entryKey.substring(1)));
                    } else {
                        if (ReUtil.isMatch(GXCommonConstants.DIGITAL_REGULAR_EXPRESSION, entryValue.toString())) {
                            sql.SET(StrUtil.format("{} = JSON_SET({} , '$.{}' , {})", dataKey, dataKey, entryKey, entryValue));
                        } else {
                            if (!ClassUtil.isPrimitiveWrapper(entryValue.getClass())
                                    && !ClassUtil.equals(entryValue.getClass(), "String", true)
                                    && (entryValue instanceof Map || entryValue instanceof GXBaseEntity)) {
                                entryValue = JSONUtil.toJsonStr(entryValue);
                            }
                            sql.SET(StrUtil.format("{} = JSON_SET({} , '$.{}' , '{}')", dataKey, dataKey, entryKey, entryValue));
                        }
                    }
                }
                continue;
            }
            if (value instanceof Map) {
                value = JSONUtil.toJsonStr(value);
            }
            if (ReUtil.isMatch(GXCommonConstants.DIGITAL_REGULAR_EXPRESSION, value.toString())) {
                sql.SET(CharSequenceUtil.format("{} " + GXBaseBuilderConstants.NUMBER_EQ, dataKey, value));
            } else {
                sql.SET(CharSequenceUtil.format("{} " + GXBaseBuilderConstants.STR_EQ, dataKey, value));
            }
        }
        final Set<String> conditionKeys = whereData.keySet();
        for (String conditionKey : conditionKeys) {
            String template = "{} " + GXBaseBuilderConstants.STR_EQ;
            final String value = whereData.getStr(conditionKey);
            if (ReUtil.isMatch(GXCommonConstants.DIGITAL_REGULAR_EXPRESSION, value)) {
                template = "{} " + GXBaseBuilderConstants.NUMBER_EQ;
            }
            sql.WHERE(CharSequenceUtil.format(template, conditionKey, value));
        }
        sql.SET(CharSequenceUtil.format("updated_at = {}", DateUtil.currentSeconds()));
        return sql.toString();
    }

    /**
     * 单独更新记录状态
     *
     * @param tableName 数据表明
     * @param status    状态值
     * @param condition 更新条件
     * @return String
     */
    static String updateStatusByCondition(String tableName, int status, Dict condition) {
        return updateFieldByCondition(tableName, Dict.create().set("status", status), condition);
    }

    /**
     * 判断给定条件的值是否存在
     *
     * @param tableName 表名
     * @param condition 条件
     * @return String
     */
    static String checkRecordIsExists(String tableName, Dict condition) {
        final SQL sql = new SQL().SELECT("1").FROM(tableName);
        final Set<String> conditionKeys = condition.keySet();
        for (String conditionKey : conditionKeys) {
            String template = "{} " + GXBaseBuilderConstants.STR_EQ;
            final String value = condition.getStr(conditionKey);
            if (ReUtil.isMatch(GXCommonConstants.DIGITAL_REGULAR_EXPRESSION, value)) {
                template = "{} " + GXBaseBuilderConstants.NUMBER_EQ;
            }
            sql.WHERE(CharSequenceUtil.format(template, conditionKey, value));
        }
        sql.LIMIT(1);
        return CharSequenceUtil.format("SELECT IFNULL(({}) , 0)", sql.toString());
    }

    /**
     * 判断给定条件的值是否存在
     *
     * @param tableName 表名
     * @param condition 条件
     * @return String
     */
    static String checkRecordIsUnique(String tableName, Dict condition) {
        return checkRecordIsExists(tableName, condition);
    }

    /**
     * 查询单表的指定字段
     *
     * @param tableName 表名
     * @param fieldSet  字段集合
     * @param condition 条件
     * @return String
     */
    static String getFieldValueBySQL(String tableName, Set<String> fieldSet, Dict condition, boolean remove) {
        final GXDBSchemaService schemaService = GXSpringContextUtils.getBean(GXDBSchemaService.class);
        assert schemaService != null;
        final String selectFieldStr = schemaService.getSelectFieldStr(tableName, fieldSet, remove);
        final SQL sql = new SQL().SELECT(selectFieldStr).FROM(tableName);
        final Set<String> conditionKeys = condition.keySet();
        for (String conditionKey : conditionKeys) {
            String template = "{} " + GXBaseBuilderConstants.STR_EQ;
            final String value = condition.getStr(conditionKey);
            if (!(PhoneUtil.isMobile(value) || PhoneUtil.isPhone(value) || PhoneUtil.isTel(value)) && ReUtil.isMatch(GXCommonConstants.DIGITAL_REGULAR_EXPRESSION, value)) {
                template = "{} " + GXBaseBuilderConstants.NUMBER_EQ;
            }
            sql.WHERE(CharSequenceUtil.format(template, conditionKey, value));
        }
        return sql.toString();
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名
     * @param fieldSet  字段集合
     * @param dataList  需要插入的数据列表
     * @return String
     */
    static String batchInsertBySQL(String tableName, Set<String> fieldSet, List<Dict> dataList) {
        if (dataList.isEmpty()) {
            return "";
        }
        final Set<String> newFieldSet = new HashSet<>(dataList.get(0).keySet());
        if (!fieldSet.retainAll(newFieldSet)) {
            fieldSet = newFieldSet;
        }
        String sql = "INSERT INTO " + tableName + "(`" + CollUtil.join(fieldSet, "`,`") + "`) VALUES ";
        StringBuilder values = new StringBuilder();
        for (Dict dict : dataList) {
            int len = dict.size();
            int key = 0;
            values.append("('");
            for (String field : fieldSet) {
                if (++key == len) {
                    values.append(dict.getObj(field)).append("'");
                } else {
                    values.append(dict.getObj(field)).append("','");
                }
            }
            values.append("),");
        }
        return sql + CharSequenceUtil.sub(values, 0, values.lastIndexOf(","));
    }

    /**
     * 列表
     *
     * @param param 参数
     * @return String
     */
    String listOrSearch(Dict param);

    /**
     * 分页列表
     *
     * @param param 参数
     * @return String
     */
    default String listOrSearchPage(IPage<Dict> page, Dict param) {
        return "";
    }

    /**
     * 详情
     *
     * @param param 参数
     * @return String
     */
    String detail(Dict param);

    /**
     * 获取请求对象中的搜索条件数据
     *
     * @param param 参数
     * @return Dict
     */
    default Dict getRequestSearchCondition(Dict param) {
        return Optional.ofNullable(Convert.convert(Dict.class, param.getObj(GXBaseBuilderConstants.SEARCH_CONDITION_NAME))).orElse(Dict.create());
    }

    /**
     * 组合时间查询SQL
     * <pre>
     *     {@code
     *     processTimeField("created_at", "created_at > {} and created_at < {}", {start=2019-12-20,end=2019-12-31})
     *     }
     * </pre>
     *
     * @param fieldName    字段名字
     * @param conditionStr 查询条件
     * @param param        参数
     * @param aliasPrefix  表的别名
     * @return String
     */
    default String processTimeField(String fieldName, String conditionStr, Object param, String aliasPrefix) {
        final String today = DateUtil.today();
        String startDate = CharSequenceUtil.format("{} 0:0:0", today);
        String endDate = CharSequenceUtil.format("{} 23:59:59", today);
        final Dict dict = Convert.convert(Dict.class, param);
        if (null != dict.getStr("start")) {
            startDate = dict.getStr("start");
        }
        if (null != dict.getStr("end")) {
            endDate = dict.getStr("end");
        }
        if (!CharSequenceUtil.contains(fieldName, ".") && CharSequenceUtil.isNotBlank(aliasPrefix)) {
            fieldName = CharSequenceUtil.format("{}.{}", aliasPrefix, fieldName);
        }
        final long start = DateUtil.parse(startDate).getTime() / 1000;
        final long end = DateUtil.parse(endDate).getTime() / 1000;
        return CharSequenceUtil.format(conditionStr, fieldName, start, fieldName, end);
    }

    /**
     * 合并搜索条件到SQL对象中
     *
     * @param sql          SQL对象
     * @param requestParam 请求参数
     * @param aliasPrefix  别名
     * @return Dict
     */
    @GXDataSourceAnnotation("framework")
    default Dict mergeSearchConditionToSQL(SQL sql, Dict requestParam, String aliasPrefix) {
        final String modelIdentificationValue = getModelIdentificationValue();
        if (CharSequenceUtil.isBlank(modelIdentificationValue)) {
            throw new GXException(CharSequenceUtil.format("请配置{}.{}的模型标识", getClass().getSimpleName(), GXBaseBuilderConstants.MODEL_IDENTIFICATION_NAME));
        }
        final Dict condition = Dict.create().set(GXBaseBuilderConstants.MODEL_IDENTIFICATION_NAME, modelIdentificationValue);
        Dict searchField = Objects.requireNonNull(GXSpringContextUtils.getBean(GXCoreModelService.class)).getSearchCondition(condition);
        searchField.putAll(getDefaultSearchField());
        Dict requestSearchCondition = getRequestSearchCondition(requestParam);
        final Dict timeFields = getTimeFields();
        Set<String> keySet = requestSearchCondition.keySet();
        for (String key : keySet) {
            key = CharSequenceUtil.toUnderlineCase(key);
            Object value = requestSearchCondition.getObj(key);
            if (Objects.isNull(value)) {
                value = requestSearchCondition.getObj(CharSequenceUtil.toCamelCase(key));
            }
            if (key.equals(GXCommonConstants.CUSTOMER_SEARCH_MIXED_FIELD_CONDITION)
                    && searchField.getStr(GXCommonConstants.CUSTOMER_SEARCH_MIXED_FIELD_CONDITION) != null) {
                sql.WHERE(CharSequenceUtil.indexedFormat(searchField.getStr(GXCommonConstants.CUSTOMER_SEARCH_MIXED_FIELD_CONDITION), "'" + value + "%'"));
                continue;
            }
            String lastKey = ReUtil.replaceAll(key, "[!<>*^$@#%&]", "");
            if (null == value || (value.getClass().getName().equalsIgnoreCase("java.lang.String") && CharSequenceUtil.isBlank(value.toString()))) {
                continue;
            }
            String operator = searchField.getStr(key);
            if (null == operator) {
                if (CharSequenceUtil.isNotBlank(aliasPrefix)) {
                    key = CharSequenceUtil.concat(true, aliasPrefix, ".", key);
                    operator = searchField.getStr(key);
                }
                if (null == operator && CharSequenceUtil.isNotBlank(timeFields.getStr(key))) {
                    operator = timeFields.getStr(key);
                    if (null == operator && CharSequenceUtil.isNotBlank(aliasPrefix)) {
                        key = CharSequenceUtil.concat(true, aliasPrefix, ".", key);
                        operator = timeFields.getStr(key);
                    }
                }
            }
            if (null == operator) {
                GXCommonUtils.getLogger(GXBaseBuilder.class).warn("{}字段没有配置搜索条件", key);
            } else {
                if (CharSequenceUtil.isNotBlank(timeFields.getStr(key))) {
                    final String s = processTimeField(key, operator, value, aliasPrefix);
                    sql.WHERE(s);
                } else {
                    if (value instanceof Collection) {
                        value = CollUtil.join((Collection<?>) value, ",");
                    }
                    if (CharSequenceUtil.isNotBlank(aliasPrefix) && !CharSequenceUtil.contains(key, ".")) {
                        sql.WHERE(CharSequenceUtil.format("`{}`.`{}` ".concat(operator), aliasPrefix, lastKey, value));
                    } else {
                        if (CharSequenceUtil.contains(key, ".")) {
                            sql.WHERE(CharSequenceUtil.format("{} ".concat(operator), lastKey, value));
                        } else {
                            sql.WHERE(CharSequenceUtil.format("`{}` ".concat(operator), lastKey, value));
                        }
                    }
                }
            }
        }
        return Dict.create();
    }

    /**
     * 合并搜索条件到SQL对象中
     *
     * @param sql          SQL对象
     * @param requestParam 请求参数
     * @return Dict
     */
    @GXDataSourceAnnotation("framework")
    default Dict mergeSearchConditionToSQL(SQL sql, Dict requestParam) {
        return mergeSearchConditionToSQL(sql, requestParam, "");
    }

    /**
     * 获取SQL语句的查询字段
     *
     * @param tableName  表名
     * @param targetSet  目标字段集合
     * @param tableAlias 表的别名
     * @param remove     是否移除
     * @return String
     */
    default String getSelectFieldStr(String tableName, Set<String> targetSet, String tableAlias, boolean remove, boolean saveJSONField) {
        final GXDBSchemaService schemaService = GXSpringContextUtils.getBean(GXDBSchemaService.class);
        assert schemaService != null;
        return schemaService.getSelectFieldStr(tableName, targetSet, tableAlias, remove, saveJSONField);
    }

    /**
     * 获取SQL语句的查询字段
     *
     * @param tableName  表名
     * @param targetSet  目标字段集合
     * @param tableAlias 表的别名
     * @param remove     是否移除
     * @return String
     */
    default String getSelectFieldStr(String tableName, Set<String> targetSet, String tableAlias, boolean remove) {
        final GXDBSchemaService schemaService = GXSpringContextUtils.getBean(GXDBSchemaService.class);
        assert schemaService != null;
        return schemaService.getSelectFieldStr(tableName, targetSet, tableAlias, remove);
    }

    /**
     * 获取SQL语句的查询字段
     *
     * @param tableName 表名
     * @param targetSet 目标字段集合
     * @param remove    是否移除
     * @return String
     */
    default String getSelectFieldStr(String tableName, Set<String> targetSet, boolean remove) {
        final GXDBSchemaService schemaService = GXSpringContextUtils.getBean(GXDBSchemaService.class);
        assert schemaService != null;
        return schemaService.getSelectFieldStr(tableName, targetSet, remove);
    }

    /**
     * 给现有查询条件新增查询条件
     *
     * @param requestParam 请求参数
     * @param key          key
     * @param value        value
     * @return Dict
     */
    default Dict addConditionToSearchCondition(Dict requestParam, String key, Object value) {
        return GXCommonUtils.addSearchCondition(requestParam, key, value, false);
    }

    /**
     * 给现有查询条件新增查询条件
     *
     * @param requestParam 请求参数
     * @param sourceData   数据源
     * @return Dict
     */
    default Dict addConditionToSearchCondition(Dict requestParam, Dict sourceData) {
        return GXCommonUtils.addSearchCondition(requestParam, sourceData, false);
    }

    /**
     * 获取时间字段配置
     *
     * @return Dict
     */
    default Dict getTimeFields() {
        return Dict.create()
                .set("created_at", GXBaseBuilderConstants.TIME_RANGE_WITH_EQ)
                .set("updated_at", GXBaseBuilderConstants.TIME_RANGE_WITH_EQ)
                .set("cancel_at", GXBaseBuilderConstants.TIME_RANGE_WITH_EQ)
                .set("complete_at", GXBaseBuilderConstants.TIME_RANGE_WITH_EQ);
    }

    /**
     * 默认的搜索条件
     *
     * @return Dict
     */
    default Dict getDefaultSearchField() {
        return Dict.create();
    }

    /**
     * 数据配置的模型标识
     *
     * @return String
     */
    String getModelIdentificationValue();
}
