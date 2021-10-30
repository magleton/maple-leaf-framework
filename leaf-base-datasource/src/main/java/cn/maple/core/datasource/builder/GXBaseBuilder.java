package cn.maple.core.datasource.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.maple.core.datasource.annotation.GXDataSource;
import cn.maple.core.datasource.constant.GXBaseBuilderConstant;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.service.GXDBSchemaService;
import cn.maple.core.datasource.util.GXDataSourceCommonUtils;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXBaseCommonUtil;
import cn.maple.core.framework.util.GXSpringContextUtil;
import com.google.common.collect.Table;
import org.apache.ibatis.jdbc.SQL;

import java.util.*;
import java.util.stream.Collectors;

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
    @SuppressWarnings("all")
    static String updateFieldByCondition(String tableName, Dict data, Dict whereData) {
        final SQL sql = new SQL().UPDATE(tableName);
        final Set<String> fieldNames = data.keySet();
        for (String fieldName : fieldNames) {
            Object value = data.getObj(fieldName);
            if (value instanceof Table) {
                Table<String, String, Object> table = Convert.convert(new TypeReference<Table<String, String, Object>>() {
                }, value);
                final Map<String, Object> row = table.row(fieldName);
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    final String entryKey = entry.getKey();
                    Object entryValue = entry.getValue();
                    if (entryKey.startsWith("-")) {
                        sql.SET(CharSequenceUtil.format("{} = JSON_REMOVE({} , '$.{}')", fieldName, fieldName, entryKey.substring(1)));
                    } else {
                        if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, entryValue.toString())) {
                            sql.SET(CharSequenceUtil.format("{} = JSON_SET({} , '$.{}' , {})", fieldName, fieldName, entryKey, entryValue));
                        } else {
                            if (!ClassUtil.isPrimitiveWrapper(entryValue.getClass())
                                    && !ClassUtil.equals(entryValue.getClass(), "String", true)
                                    && (entryValue instanceof Map || entryValue instanceof GXBaseEntity)) {
                                entryValue = JSONUtil.toJsonStr(entryValue);
                            }
                            sql.SET(CharSequenceUtil.format("{} = JSON_SET({} , '$.{}' , '{}')", fieldName, fieldName, entryKey, entryValue));
                        }
                    }
                }
                continue;
            }
            if (value instanceof Map) {
                value = JSONUtil.toJsonStr(value);
            }
            if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value.toString())) {
                sql.SET(CharSequenceUtil.format("{} " + GXBaseBuilderConstant.NUMBER_EQ, fieldName, value));
            } else {
                sql.SET(CharSequenceUtil.format("{} " + GXBaseBuilderConstant.STR_EQ, fieldName, value));
            }
        }
        whereData.keySet().forEach(conditionKey -> {
            String template = "{} " + GXBaseBuilderConstant.STR_EQ;
            final String value = whereData.getStr(conditionKey);
            if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value)) {
                template = "{} " + GXBaseBuilderConstant.NUMBER_EQ;
            }
            sql.WHERE(CharSequenceUtil.format(template, conditionKey, value));
        });
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
            String template = "{} " + GXBaseBuilderConstant.STR_EQ;
            final String value = condition.getStr(conditionKey);
            if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value)) {
                template = "{} " + GXBaseBuilderConstant.NUMBER_EQ;
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
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名
     * @param fieldSet  字段集合
     * @param dataList  需要插入的数据列表
     * @return String
     */
    static String batchInsertBySql(String tableName, Set<String> fieldSet, List<Dict> dataList) {
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
     * 查询单表的指定字段
     *
     * @param tableName 表名
     * @param fieldSet  字段集合
     * @param condition 条件
     * @return String
     */
    static String getFieldValueBySql(String tableName, Set<String> fieldSet, Dict condition, boolean remove) {
        final GXDBSchemaService schemaService = GXSpringContextUtil.getBean(GXDBSchemaService.class);
        assert schemaService != null;
        final String selectFieldStr = schemaService.getSelectFieldStr(tableName, fieldSet, remove);
        final SQL sql = new SQL().SELECT(selectFieldStr).FROM(tableName);
        final Set<String> conditionKeys = condition.keySet();
        for (String conditionKey : conditionKeys) {
            String template = "{} " + GXBaseBuilderConstant.STR_EQ;
            final String value = condition.getStr(conditionKey);
            if (!(PhoneUtil.isMobile(value) || PhoneUtil.isPhone(value) || PhoneUtil.isTel(value)) && ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value)) {
                template = "{} " + GXBaseBuilderConstant.NUMBER_EQ;
            }
            sql.WHERE(CharSequenceUtil.format(template, conditionKey, value));
        }
        return sql.toString();
    }

    /**
     * 通过条件获取分类数据
     *
     * @param tableName 表名字
     * @param fieldSet  需要查询的字段
     * @param condition 条件
     *                  <code>
     *                  Table<String, String, Object> condition = HashBasedTable.create();
     *                  condition.put("path" , "like" , "aaa%");
     *                  condition.put("path" , "in" , "(1,2,3,4,5,6)");
     *                  condition.put("level" , "=" , "1111");
     *                  getDataByCondition("test" , CollUtil.newHashSet("id" , "username"), condition);
     *                  </code>
     * @return SQL语句
     */
    static String getDataByCondition(String tableName, Set<String> fieldSet, Table<String, String, Object> condition) {
        String selectStr = "*";
        if (CollUtil.isNotEmpty(fieldSet)) {
            selectStr = String.join(",", fieldSet);
        }
        SQL sql = new SQL().SELECT(selectStr).FROM(tableName);
        Map<String, Map<String, Object>> conditionMap = condition.rowMap();
        conditionMap.forEach((column, datum) -> {
            List<String> wheres = new ArrayList<>();
            datum.forEach((operator, value) -> {
                wheres.add(CharSequenceUtil.format("{} {} {}", column, operator, value));
                wheres.add("or");
            });
            wheres.remove(wheres.size() - 1);
            String whereStr = String.join(" ", wheres);
            sql.WHERE(whereStr);
        });
        return sql.toString();
    }

    /**
     * 分页列表
     *
     * @param param 参数
     * @return String
     */
    default <R> String listOrSearchPage(IPage<R> page, Dict param) {
        throw new GXBusinessException("请实现自定义的listOrSearchPage方法");
    }

    /**
     * 获取请求对象中的搜索条件数据
     *
     * @param param 参数
     * @return Dict
     */
    @SuppressWarnings("all")
    default Dict getRequestSearchCondition(Dict param) {
        Dict searchConditionDict = Convert.convert(Dict.class, param.getObj(GXBaseBuilderConstant.SEARCH_CONDITION_NAME));
        Dict retDict = Dict.create();
        if (Objects.isNull(searchConditionDict)) {
            return retDict;
        }
        searchConditionDict.forEach((key, value) -> {
            if (Objects.nonNull(value)) {
                String className = value.getClass().getName();
                if (CharSequenceUtil.containsIgnoreCase(className, "java.util.ArrayList")) {
                    List<Object> list = Convert.toList(Object.class, value);
                    if (!list.isEmpty()) {
                        retDict.set(key, value);
                    }
                } else if (CharSequenceUtil.containsIgnoreCase(className, "java.util.LinkedHashMap")) {
                    Map<String, Object> map = Convert.toMap(String.class, Object.class, value);
                    if (!map.isEmpty()) {
                        retDict.set(key, value);
                    }
                } else if (CharSequenceUtil.containsIgnoreCase(className, "java.lang.String")
                        && CharSequenceUtil.isNotBlank(value.toString())) {
                    retDict.set(key, value);
                } else {
                    retDict.set(key, value);
                }
            }
        });

        return retDict;
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
     * @return String
     */
    default String processTimeField(String fieldName, String conditionStr, Object param) {
        final String today = DateUtil.today();
        Long start = DateUtil.parse(CharSequenceUtil.format("{} 0:0:0", today)).getTime();
        Long end = DateUtil.parse(CharSequenceUtil.format("{} 23:59:59", today)).getTime();
        final Dict dict = Convert.convert(Dict.class, param);
        if (null != dict.getLong("start")) {
            start = dict.getLong("start");
        }
        if (null != dict.getLong("end")) {
            end = dict.getLong("end");
        }
        return CharSequenceUtil.format(conditionStr, fieldName, start, fieldName, end);
    }

    /**
     * 给现有查询条件新增查询条件
     *
     * @param requestParam 请求参数
     * @param key          key
     * @param value        value
     * @return Dict
     */
    default Dict addSearchCondition(Dict requestParam, String key, Object value) {
        return GXDataSourceCommonUtils.addSearchCondition(requestParam, key, value, false);
    }

    /**
     * 给现有查询条件新增查询条件
     *
     * @param requestParam 请求参数
     * @param sourceData   数据源
     * @return Dict
     */
    default Dict addSearchCondition(Dict requestParam, Dict sourceData) {
        return GXDataSourceCommonUtils.addSearchCondition(requestParam, sourceData, false);
    }

    /**
     * 获取时间字段配置
     *
     * @return Dict
     */
    default Dict getTimeFields() {
        return Dict.create()
                .set("createdAt", GXBaseBuilderConstant.TIME_RANGE_WITH_EQ)
                .set("updatedAt", GXBaseBuilderConstant.TIME_RANGE_WITH_EQ)
                .set("cancelAt", GXBaseBuilderConstant.TIME_RANGE_WITH_EQ)
                .set("payAt", GXBaseBuilderConstant.TIME_RANGE_WITH_EQ)
                .set("completeAt", GXBaseBuilderConstant.TIME_RANGE_WITH_EQ);
    }

    /**
     * 合并搜索条件到SQL对象中
     *
     * @param sql                      SQL对象
     * @param requestParam             请求参数
     * @param isMergeDBSearchCondition 是否合并数据库配置的搜索条件
     */
    @SuppressWarnings("all")
    @GXDataSource("framework")
    default void mergeSearchConditionToSql(SQL sql, Dict requestParam, Boolean isMergeDBSearchCondition) {
        final String modelIdentificationValue = getModelIdentificationValue();
        if (CharSequenceUtil.isBlank(modelIdentificationValue)) {
            throw new GXBusinessException(CharSequenceUtil.format("请配置{}.{}的模型标识", getClass().getSimpleName(), GXBaseBuilderConstant.MODEL_IDENTIFICATION_NAME));
        }
        Dict searchField = getDefaultSearchField();
        Dict requestSearchCondition = getRequestSearchCondition(requestParam);
        final Dict timeFields = getTimeFields();
        Set<String> keySet = requestSearchCondition.keySet();
        for (String key : keySet) {
            boolean timeFieldFlag = false;
            String underLineKey = CharSequenceUtil.toUnderlineCase(key);
            Object value = Optional.ofNullable(requestSearchCondition.getObj(key)).orElse(requestSearchCondition.getObj(underLineKey));
            boolean flag = (value instanceof Collection) && !((Collection<?>) value).isEmpty();
            if (Objects.nonNull(value) && (flag || CharSequenceUtil.isNotBlank(value.toString()))) {
                String operator = Optional.ofNullable(searchField.getStr(key)).orElse(searchField.getStr(underLineKey));
                if (Objects.isNull(operator)) {
                    operator = Optional.ofNullable(timeFields.getStr(key)).orElse(timeFields.getStr(underLineKey));
                    timeFieldFlag = true;
                }
                if (Objects.isNull(operator)) {
                    GXBaseCommonUtil.getLogger(GXBaseBuilder.class).warn("{}字段没有配置搜索条件", underLineKey);
                    continue;
                }
                if (timeFieldFlag) {
                    final String s = processTimeField(underLineKey, operator, value);
                    sql.WHERE(s);
                    continue;
                }
                if (flag) {
                    value = CollUtil.join(((Collection<?>) value).stream().map(d -> {
                        if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, d.toString())) {
                            return d;
                        }
                        return "\"" + d + "\"";
                    }).collect(Collectors.toSet()), ",");
                }
                String lastKey = ReUtil.replaceAll(underLineKey, "[!<>*^$@#%&]", "");
                if (CharSequenceUtil.contains(underLineKey, ".")) {
                    sql.WHERE(CharSequenceUtil.format("({} ".concat(operator) + ")", lastKey, value));
                    continue;
                }
                sql.WHERE(CharSequenceUtil.format("(`{}` ".concat(operator) + ")", lastKey, value));
            }
        }
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
    default String getSelectField(String tableName, Set<String> targetSet, String tableAlias, boolean remove, boolean saveJSONField) {
        final GXDBSchemaService schemaService = GXSpringContextUtil.getBean(GXDBSchemaService.class);
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
    default String getSelectField(String tableName, Set<String> targetSet, String tableAlias, boolean remove) {
        final GXDBSchemaService schemaService = GXSpringContextUtil.getBean(GXDBSchemaService.class);
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
    default String getSelectField(String tableName, Set<String> targetSet, boolean remove) {
        final GXDBSchemaService schemaService = GXSpringContextUtil.getBean(GXDBSchemaService.class);
        assert schemaService != null;
        return schemaService.getSelectFieldStr(tableName, targetSet, remove);
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