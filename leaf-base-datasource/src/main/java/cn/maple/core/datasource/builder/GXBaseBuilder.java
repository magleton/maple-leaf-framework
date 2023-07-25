package cn.maple.core.datasource.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.constant.GXBuilderConstant;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.GXJoinDto;
import cn.maple.core.framework.dto.inner.GXJoinTypeEnums;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.condition.GXConditionExclusionDeletedField;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.inner.op.GXDbJoinOp;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public interface GXBaseBuilder {
    Logger LOGGER = LoggerFactory.getLogger(GXBaseBuilder.class);

    /**
     * 更新实体字段和虚拟字段
     *
     * @param tableName 表名
     * @param fieldList 数据列表
     * @param condition 条件
     * @return String
     */
    static String updateFieldByCondition(String tableName, List<GXUpdateField<?>> fieldList, List<GXCondition<?>> condition) {
        final SQL sql = new SQL().UPDATE(tableName);
        for (GXUpdateField<?> field : fieldList) {
            sql.SET(field.updateString());
        }
        sql.SET(CharSequenceUtil.format("updated_at = {}", DateUtil.currentSeconds()));
        handleSQLCondition(sql, condition);
        if (!CollUtil.contains(condition, (c -> GXConditionExclusionDeletedField.class.isAssignableFrom(c.getClass())))) {
            sql.WHERE(CharSequenceUtil.format("{}.is_deleted = {}", tableName, getIsNotDeletedValue()));
        }
        return sql.toString();
    }

    /**
     * 判断给定条件的值是否存在
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return String
     */
    static String checkRecordIsExists(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        dbQueryParamInnerDto.setLimit(1);
        dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("1"));
        return findOneByCondition(dbQueryParamInnerDto);
    }

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return SQL语句
     */
    static String findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        Set<String> columns = dbQueryParamInnerDto.getColumns();
        String tableName = dbQueryParamInnerDto.getTableName();
        String tableNameAlias = Optional.ofNullable(dbQueryParamInnerDto.getTableNameAlias()).orElse(tableName);
        Set<String> groupByField = dbQueryParamInnerDto.getGroupByField();
        Map<String, String> orderByField = dbQueryParamInnerDto.getOrderByField();
        Set<String> having = dbQueryParamInnerDto.getHaving();
        Integer limit = dbQueryParamInnerDto.getLimit();
        String selectStr = CharSequenceUtil.format("{}.*", tableNameAlias);
        if (CollUtil.isNotEmpty(columns)) {
            List<String> columnsCollect = columns.stream().map(CharSequenceUtil::toUnderlineCase).collect(Collectors.toList());
            selectStr = String.join(",", columnsCollect);
        }
        SQL sql = new SQL().SELECT(selectStr).FROM(CharSequenceUtil.format("{} {}", tableName, tableNameAlias));
        // 处理JOIN
        List<GXJoinDto> joins = dbQueryParamInnerDto.getJoins();
        if (Objects.nonNull(joins) && !joins.isEmpty()) {
            handleSQLJoin(sql, joins);
        }
        List<GXCondition<?>> condition = dbQueryParamInnerDto.getCondition();
        // 处理WHERE
        handleSQLCondition(sql, condition);
        if (!CollUtil.contains(condition, (c -> GXConditionExclusionDeletedField.class.isAssignableFrom(c.getClass())))) {
            sql.WHERE(CharSequenceUtil.format("{}.is_deleted = {}", tableNameAlias, getIsNotDeletedValue()));
        }
        // 处理分组
        if (CollUtil.isNotEmpty(groupByField)) {
            sql.GROUP_BY(groupByField.toArray(new String[0]));
        }
        // 处理HAVING
        if (CollUtil.isNotEmpty(having)) {
            sql.HAVING(having.toArray(new String[0]));
        }
        // 处理排序
        if (Objects.nonNull(orderByField) && !orderByField.isEmpty()) {
            String[] orderColumns = new String[orderByField.size()];
            Integer[] idx = new Integer[]{0};
            orderByField.forEach((k, v) -> orderColumns[idx[0]++] = CharSequenceUtil.format("{} {}", k, v));
            sql.ORDER_BY(orderColumns);
        }
        // 处理LIMIT
        if (Objects.nonNull(limit) && limit > 0) {
            sql.LIMIT(limit);
        }
        return sql.toString();
    }

    /**
     * 处理JOIN表
     *
     * @param sql   SQL语句
     * @param joins joins信息
     */
    static void handleSQLJoin(SQL sql, List<GXJoinDto> joins) {
        joins.forEach(join -> {
            GXJoinTypeEnums joinType = join.getJoinType();
            String tableName = join.getJoinTableName();
            String tableAliasName = join.getJoinTableNameAlias();
            String masterTableName = join.getMasterTableName();
            String masterTableNameAlias = join.getMasterTableNameAlias();
            if (Objects.isNull(masterTableNameAlias)) {
                masterTableNameAlias = masterTableName;
            }
            String andClause = Optional.ofNullable(join.getAnd()).orElse(Collections.emptyList()).stream().map(GXDbJoinOp::opString).collect(Collectors.joining(GXBuilderConstant.AND_OP));
            String orClause = Optional.ofNullable(join.getOr()).orElse(Collections.emptyList()).stream().map(GXDbJoinOp::opString).collect(Collectors.joining(GXBuilderConstant.AND_OP));
            String assemblySql = CharSequenceUtil.format("{} {} ON ({})", masterTableName, masterTableNameAlias, andClause);
            if (CharSequenceUtil.isNotEmpty(orClause)) {
                assemblySql = CharSequenceUtil.format("{} {} ({})", assemblySql, GXBuilderConstant.OR_OP, orClause);
            }
            if (CharSequenceUtil.equalsIgnoreCase(GXBuilderConstant.LEFT_JOIN_TYPE, joinType.getJoinType())) {
                sql.LEFT_OUTER_JOIN(assemblySql);
            } else if (CharSequenceUtil.equalsIgnoreCase(GXBuilderConstant.RIGHT_JOIN_TYPE, joinType.getJoinType())) {
                sql.RIGHT_OUTER_JOIN(assemblySql);
            } else if (CharSequenceUtil.equalsIgnoreCase(GXBuilderConstant.INNER_JOIN_TYPE, joinType.getJoinType())) {
                sql.INNER_JOIN(assemblySql);
            }
        });
    }

    /**
     * 通过条件获取分类数据
     *
     * @param page                 分页对象
     * @param dbQueryParamInnerDto 查询对象
     * @return SQL语句
     */
    @SuppressWarnings("unused")
    static <R> String paginate(IPage<R> page, GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        if (CharSequenceUtil.isNotBlank(dbQueryParamInnerDto.getRawSQL())) {
            return dbQueryParamInnerDto.getRawSQL();
        }
        return findByCondition(dbQueryParamInnerDto);
    }

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return SQL语句
     */
    static String findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        int limit = Optional.ofNullable(dbQueryParamInnerDto.getLimit()).orElse(1);
        if (limit <= 0) {
            limit = 1;
        }
        dbQueryParamInnerDto.setLimit(limit);
        return findByCondition(dbQueryParamInnerDto);
    }

    /**
     * 处理SQL语句的Where条件
     *
     * @param sql       SQL对象
     * @param condition 条件
     */
    static void handleSQLCondition(SQL sql, List<GXCondition<?>> condition) {
        if (Objects.isNull(condition) || condition.isEmpty()) {
            return;
        }
        List<String> lastWheres = new ArrayList<>();
        condition.forEach(c -> {
            if (!GXConditionExclusionDeletedField.class.isAssignableFrom(c.getClass())) {
                String str = c.whereString();
                if (CharSequenceUtil.isNotEmpty(str)) {
                    lastWheres.add(str);
                }
            }
        });
        if (!lastWheres.isEmpty()) {
            String whereStr = String.join(" AND ", lastWheres);
            sql.WHERE(whereStr);
        }
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return SQL语句
     */
    static String deleteSoftCondition(String tableName, List<GXCondition<?>> condition) {
        SQL sql = new SQL().UPDATE(tableName);
        sql.SET("is_deleted = id", CharSequenceUtil.format("deleted_at = {}", DateUtil.currentSeconds()));
        handleSQLCondition(sql, condition);
        if (!CollUtil.contains(condition, (c -> GXConditionExclusionDeletedField.class.isAssignableFrom(c.getClass())))) {
            sql.WHERE(CharSequenceUtil.format("{}.is_deleted = {}", tableName, getIsNotDeletedValue()));
        }
        return sql.toString();
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return SQL语句
     */
    static String deleteCondition(String tableName, List<GXCondition<?>> condition) {
        SQL sql = new SQL().DELETE_FROM(tableName);
        handleSQLCondition(sql, condition);
        if (!CollUtil.contains(condition, (c -> GXConditionExclusionDeletedField.class.isAssignableFrom(c.getClass())))) {
            sql.WHERE(CharSequenceUtil.format("{}.is_deleted = {}", tableName, getIsNotDeletedValue()));
        }
        return sql.toString();
    }

    /**
     * 获取数据库未删除的值
     *
     * @return Object
     */
    static Object getIsNotDeletedValue() {
        String notDeletedValueType = GXCommonUtils.getEnvironmentValue("notDeletedValueType", String.class, "");
        if (CharSequenceUtil.equalsIgnoreCase("string", notDeletedValueType)) {
            return GXCommonConstant.NOT_STR_DELETED_MARK;
        }
        return GXCommonConstant.NOT_INT_DELETED_MARK;
    }
}
