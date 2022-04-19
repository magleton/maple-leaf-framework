package cn.maple.core.datasource.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.constant.GXBuilderConstant;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.GXJoinDto;
import cn.maple.core.framework.dto.inner.GXJoinTypeEnums;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.inner.op.GXDbJoinOp;
import cn.maple.core.framework.filter.GXSQLFilter;
import cn.maple.core.framework.util.GXLoggerUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Table;
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
     * <pre>
     * {@code
     * final Table<String, String, Object> extData = HashBasedTable.create();
     * extData.put("ext", "name", "jack");
     * extData.put("ext", "address", "四川成都");
     * extData.put("ext", GXBuilderConstant.REMOVE_JSON_FIELD_PREFIX_FLAG + "salary" , "");
     * final Dict data = Dict.create().set("category_name", "打折商品").set("ext", extData);
     * Table<String , String , Object> condition = HashBasedTable.create();
     * condition.put("category_id" ,  GXBuilderConstant.STR_EQ , 11111);
     * updateFieldByCondition("s_category", data, condition);
     * }
     * </pre>
     *
     * @param tableName 表名
     * @param fieldList 数据
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
        return sql.toString();
    }

    /**
     * 判断给定条件的值是否存在
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return String
     */
    static String checkRecordIsExists(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        String tableName = dbQueryParamInnerDto.getTableName();
        String tableNameAlias = Optional.ofNullable(dbQueryParamInnerDto.getTableNameAlias()).orElse(tableName);
        List<GXCondition<?>> condition = dbQueryParamInnerDto.getCondition();
        final SQL sql = new SQL().SELECT("1").FROM(tableName);
        handleSQLCondition(sql, condition);
        sql.LIMIT(1);
        return sql.toString();
    }

    /**
     * 通过条件获取数据列表
     * <pre>
     * {@code
     * Table<String, String, Object> condition = HashBasedTable.create();
     * condition.put("path" , "like" , "aaa%");
     * condition.put("path" , "in" , "(1,2,3,4,5,6)");
     * condition.put("level" , "=" , "1111");
     * GXDBQueryInnerDto queryInnerDto = GXDBQueryInnerDto.builder()
     * .columns(CollUtil.newHashSet("*"))
     * .tableName("s_admin")
     * .condition(condition)
     * .groupByField(CollUtil.newHashSet("created_at", "nickname"))
     * .orderByField(Dict.create().set("username", "asc").set("nickname", "desc")).build();
     * findByCondition(queryInnerDto);
     * Table<String, String, Object> condition = HashBasedTable.create();
     * condition.put("T_FUNC" , "JSON_OVERLAPS" , "items->'$.zipcode', CAST('[94536]' AS JSON)");
     * Table<String, String, Object> joins = HashBasedTable.create();
     * HashBasedTable<String, String, Dict> joinAdmin = HashBasedTable.create();
     * joinAdmin.put("子表别名", "主表别名", Dict.create()
     * .set("子表字段名字A", "主表表字段名字A")
     * .set("子表字段名字B", "主表表字段名字B")
     * .set("子表字段名字C", "主表表字段名字C"));
     * joins.put("链接类型A", "子表名字A", joinAdmin);
     * GXDBQueryInnerDto queryInnerDto = GXDBQueryInnerDto.builder()
     * .columns(CollUtil.newHashSet("id" , "username"))
     * .tableName("s_admin")
     * .tableNameAlias("admin")
     * .joins(joins)
     * .condition(condition)
     * .groupByField(CollUtil.newHashSet("created_at", "nickname"))
     * .orderByField(Dict.create().set("username", "asc").set("nickname", "desc")).build();
     * findByCondition(queryInnerDto);
     * }
     * </pre>
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
            selectStr = String.join(",", columns);
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
            String andClause = Optional.ofNullable(join.getAnd()).orElse(Collections.emptyList()).stream().map(GXDbJoinOp::opString).collect(Collectors.joining(GXBuilderConstant.AND_OP));
            String orClause = Optional.ofNullable(join.getOr()).orElse(Collections.emptyList()).stream().map(GXDbJoinOp::opString).collect(Collectors.joining(GXBuilderConstant.AND_OP));
            String assemblySql = CharSequenceUtil.format("{} {} ON ({})", tableName, tableAliasName, andClause);
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
     * <pre>
     * {@code
     * Table<String, String, Object> condition = HashBasedTable.create();
     * condition.put("path" , "like" , "aaa%");
     * condition.put("path" , "in" , "(1,2,3,4,5,6)");
     * condition.put("level" , "=" , "1111");
     * GXDBQueryInnerDto queryInnerDto = GXDBQueryInnerDto.builder()
     * .columns(CollUtil.newHashSet("*"))
     * .tableName("s_admin")
     * .condition(condition)
     * .groupByField(CollUtil.newHashSet("created_at", "nickname"))
     * .orderByField(Dict.create().set("username", "asc").set("nickname", "desc")).build();
     * paginate(page , queryInnerDto);
     * Table<String, String, Object> condition = HashBasedTable.create();
     * condition.put("T_FUNC" , "JSON_OVERLAPS" , "items->'$.zipcode', CAST('[94536]' AS JSON)");
     * GXDBQueryInnerDto queryInnerDto = GXDBQueryInnerDto.builder()
     * .columns(CollUtil.newHashSet("id" , "username"))
     * .tableName("s_admin")
     * .condition(condition)
     * .groupByField(CollUtil.newHashSet("created_at", "nickname"))
     * .orderByField(Dict.create().set("username", "asc").set("nickname", "desc")).build();
     * paginate(page , queryInnerDto);
     * }
     * </pre>
     *
     * @param page                 分页对象
     * @param dbQueryParamInnerDto 查询对象
     * @return SQL语句
     */
    @SuppressWarnings("unused")
    static <R> String paginate(IPage<R> page, GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return findByCondition(dbQueryParamInnerDto);
    }

    /**
     * 通过条件获取数据列表
     *
     * <pre>
     * {@code
     * Table<String, String, Object> condition = HashBasedTable.create();
     * condition.put("path" , "like" , "aaa%");
     * condition.put("path" , "in" , "(1,2,3,4,5,6)");
     * condition.put("level" , "=" , "1111");
     * findByCondition("test" , condition, CollUtil.newHashSet("id" , "username"));
     * Table<String, String, Object> condition = HashBasedTable.create();
     * condition.put("T_FUNC" , "JSON_OVERLAPS" , "items->'$.zipcode', CAST('[94536]' AS JSON)");
     * GXDBQueryInnerDto queryInnerDto = GXDBQueryInnerDto.builder()
     * .tableName("test")
     * .condition(condition)
     * .columns( CollUtil.newHashSet("id" , "username")).build();
     * findByCondition(queryInnerDto);
     * }
     * </pre>
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
            String str = c.whereString();
            lastWheres.add(str);
        });
        if (!lastWheres.isEmpty()) {
            String whereStr = String.join(" AND ", lastWheres);
            sql.WHERE(whereStr);
        }
    }


    /**
     * 处理where值为Table类型,一般为where条件后面跟函数调用
     *
     * @param wheres   where条件列表
     * @param operator 操作
     * @param value    值
     */
    private static void handleWhereFuncValue(List<String> wheres, String operator, Object value) {
        String whereStr;
        if (value instanceof Table) {
            Table<String, String, Object> table = Convert.convert(new TypeReference<>() {
            }, value);
            Map<String, Map<String, Object>> rowMap = table.rowMap();
            rowMap.forEach((c, op) -> op.forEach((k, v) -> wheres.add(CharSequenceUtil.format(CharSequenceUtil.format("{} ({}) {} ", operator, c, k), v.toString()))));
        } else if (value instanceof Set) {
            Set<String> stringSet = Convert.toSet(String.class, value);
            stringSet.forEach(v -> wheres.add(CharSequenceUtil.format("{} ({}) ", operator, v)));
        } else {
            whereStr = CharSequenceUtil.format("{} ({}) ", operator, value.toString());
            wheres.add(whereStr);
        }
    }

    /**
     * 处理where值为常规字段
     * eg: String , Number , Set
     *
     * @param tableNameAlias 表别名
     * @param column         字段
     * @param wheres         where条件列表
     * @param operator       操作
     * @param value          where的值
     */
    private static void handleWhereValue(String tableNameAlias, String column, List<String> wheres, String operator, Object value) {
        String whereColumnName = handleWhereColumn(column, tableNameAlias);
        String format = "";
        String lastValueStr = "";
        if (value instanceof String) {
            if (!CharSequenceUtil.startWith(operator, "STR_")) {
                GXLoggerUtils.logDebug(LOGGER, "SQL语句会发生隐式类型转换,请修改!!!");
            }
            if (CharSequenceUtil.isNotEmpty(value.toString())) {
                GXLoggerUtils.logDebug(LOGGER, "SQL语句优化了空字符串查询");
            }
            if (CharSequenceUtil.isNotEmpty(value.toString())) {
                format = "{} " + CharSequenceUtil.replace(operator, "STR_", "");
                lastValueStr = GXSQLFilter.sqlInject(value.toString());
            }
        } else if (value instanceof Number) {
            if (CharSequenceUtil.startWith(operator, "STR_")) {
                GXLoggerUtils.logDebug(LOGGER, "SQL语句会发生隐式类型转换,请修改");
            }
            format = "{} " + CharSequenceUtil.replace(operator, "STR_", "");
            lastValueStr = GXSQLFilter.sqlInject(value.toString());
        } else if (value instanceof Set) {
            String[] tmpFormat = {"{}"};
            if (CharSequenceUtil.startWith(operator, "STR_")) {
                tmpFormat[0] = "'{}'";
            }
            Set<Object> values = Convert.convert(new TypeReference<>() {
            }, value);
            lastValueStr = values.stream().map(str -> CharSequenceUtil.format(CharSequenceUtil.format("{}", tmpFormat[0]), str)).collect(Collectors.joining(","));
            format = "{} " + CharSequenceUtil.replace(operator, "STR_", "");
        } else {
            format = "{} " + CharSequenceUtil.replace(operator, "STR_", "");
            lastValueStr = GXSQLFilter.sqlInject(value.toString());
        }
        if (CharSequenceUtil.isNotBlank(lastValueStr)) {
            String whereStr = CharSequenceUtil.format(format, whereColumnName, lastValueStr);
            wheres.add(whereStr);
        }
    }

    /**
     * 处理where条件的字段列名字
     *
     * @param column         字段名字
     * @param tableNameAlias 表的别名
     * @return String
     */
    static String handleWhereColumn(String column, String tableNameAlias) {
        if (CharSequenceUtil.isNotEmpty(tableNameAlias) && !CharSequenceUtil.contains(column, '.')) {
            column = CharSequenceUtil.format("{}.{}", tableNameAlias, column);
        }
        return column;
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
        return sql.toString();
    }
}
