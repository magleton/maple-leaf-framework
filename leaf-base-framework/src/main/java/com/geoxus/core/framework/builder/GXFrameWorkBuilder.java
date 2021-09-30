package com.geoxus.core.framework.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.ReUtil;
import com.geoxus.common.constant.GXCommonConstant;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.common.util.GXBaseCommonUtil;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.core.builder.GXBaseBuilder;
import com.geoxus.core.constant.GXBaseBuilderConstant;
import com.geoxus.core.datasource.annotation.GXDataSource;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.core.service.GXDBSchemaService;
import org.apache.ibatis.jdbc.SQL;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface GXFrameWorkBuilder extends GXBaseBuilder {
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
        if (isMergeDBSearchCondition) {
            final Dict condition = Dict.create().set(GXBaseBuilderConstant.MODEL_IDENTIFICATION_NAME, modelIdentificationValue);
            Dict dbSearchCondition = Objects.requireNonNull(GXSpringContextUtil.getBean(GXCoreModelService.class)).getSearchCondition(condition);
            searchField.putAll(dbSearchCondition);
        }
        //searchField.putAll(getDefaultSearchField());
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
}
