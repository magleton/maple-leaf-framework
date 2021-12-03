package cn.maple.core.datasource.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.datasource.constant.GXBuilderConstant;
import cn.maple.core.datasource.dto.inner.GXDBQueryParamInnerDto;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.service.GXDBSchemaService;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.filter.GXSQLFilter;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.core.framework.util.GXValidatorUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Table;
import org.apache.ibatis.jdbc.SQL;

import java.util.*;

public interface GXBaseBuilder {
    /**
     * 更新实体字段和虚拟字段
     * <pre>
     * {@code
     * final Table<String, String, Object> extData = HashBasedTable.create();
     * extData.put("ext", "name", "jack");
     * extData.put("ext", "address", "四川成都");
     * extData.put("ext", "-salary" , "")
     * final Dict data = Dict.create().set("category_name", "打折商品").set("ext", extData);
     * updateFieldByCondition("s_category", data, Dict.create().set("category_id", 2));
     * }
     * </pre>
     *
     * @param tableName 表名
     * @param data      数据
     * @param whereData 条件
     * @return String
     */
    @SuppressWarnings("all")
    static String updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
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
                sql.SET(CharSequenceUtil.format("{} " + GXBuilderConstant.NUMBER_EQ, fieldName, value));
            } else {
                sql.SET(CharSequenceUtil.format("{} " + GXBuilderConstant.STR_EQ, fieldName, value));
            }
        }
        sql.SET(CharSequenceUtil.format(CharSequenceUtil.format("updated_at = {}", DateUtil.currentSeconds())));
        handleSQLWhereCondition(sql, condition, "");
        return sql.toString();
    }

    /**
     * 判断给定条件的值是否存在
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return String
     */
    static String checkRecordIsExists(GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        String tableName = dbQueryParamInnerDto.getTableName();
        String tableNameAlias = Optional.ofNullable(dbQueryParamInnerDto.getTableNameAlias()).orElse(tableName);
        Table<String, String, Object> condition = dbQueryParamInnerDto.getCondition();
        final SQL sql = new SQL().SELECT("1").FROM(tableName);
        handleSQLWhereCondition(sql, condition, tableNameAlias);
        sql.LIMIT(1);
        return CharSequenceUtil.format("SELECT IFNULL(({}) , 0)", sql.toString());
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名
     * @param dataList  需要插入的数据列表
     * @return String
     */
    static String batchInsert(String tableName, List<Dict> dataList) {
        if (dataList.isEmpty()) {
            throw new GXBusinessException("批量插入数据为空");
        }
        dataList.forEach(dict -> {
            dict.set("created_at", DateUtil.currentSeconds());
            if (Objects.isNull(dict.getObj("ext"))) {
                dict.set("ext", "{}");
            }
        });
        final Set<String> fieldSet = new HashSet<>(dataList.get(0).keySet());
        String sql = "INSERT INTO " + tableName + "(`" + CollUtil.join(fieldSet, "`,`") + "`) VALUES ";
        StringBuilder values = new StringBuilder();
        for (Dict dict : dataList) {
            int len = dict.size();
            int key = 0;
            values.append("('");
            for (String field : fieldSet) {
                Object value = dict.getObj(field);
                if (!ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value.toString())) {
                    value = GXSQLFilter.sqlInject(value.toString());
                }
                if (++key == len) {
                    values.append(value).append("'");
                } else {
                    values.append(value).append("','");
                }
            }
            values.append("),");
        }
        return sql + CharSequenceUtil.sub(values, 0, values.lastIndexOf(","));
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
    static String findByCondition(GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        Set<String> columns = dbQueryParamInnerDto.getColumns();
        String tableName = dbQueryParamInnerDto.getTableName();
        String tableNameAlias = Optional.ofNullable(dbQueryParamInnerDto.getTableNameAlias()).orElse(tableName);
        Set<String> groupByField = dbQueryParamInnerDto.getGroupByField();
        Dict orderByField = dbQueryParamInnerDto.getOrderByField();
        String selectStr = CharSequenceUtil.format("{}.*", tableNameAlias);
        if (CollUtil.isNotEmpty(columns)) {
            selectStr = String.join(",", columns);
        }
        SQL sql = new SQL().SELECT(selectStr).FROM(CharSequenceUtil.format("{} {}", tableName, tableNameAlias));
        Table<String, String, Table<String, String, Dict>> joins = dbQueryParamInnerDto.getJoins();
        if (Objects.nonNull(joins) && !joins.isEmpty()) {
            handleSQLJoin(sql, joins);
        }
        Table<String, String, Object> condition = dbQueryParamInnerDto.getCondition();
        if (Objects.nonNull(condition) && !condition.isEmpty()) {
            handleSQLWhereCondition(sql, condition, tableNameAlias);
        }
        sql.WHERE(CharSequenceUtil.format("{}.is_deleted = {}", tableNameAlias, getIsNotDeletedValue()));
        // 处理分组
        if (CollUtil.isNotEmpty(groupByField)) {
            sql.GROUP_BY(groupByField.toArray(new String[0]));
        }
        // 处理排序
        if (Objects.nonNull(orderByField) && !orderByField.isEmpty()) {
            String[] orderColumns = new String[orderByField.size()];
            Integer[] idx = new Integer[]{0};
            orderByField.forEach((k, v) -> orderColumns[idx[0]++] = CharSequenceUtil.format("{} {}", k, v));
            sql.ORDER_BY(orderColumns);
        }
        return sql.toString();
    }

    /**
     * 处理JOIN表
     * <pre>
     * {@code
     * Table<String, String, Object> joins = HashBasedTable.create();
     * HashBasedTable<String, String, Dict> joinAdmin = HashBasedTable.create();
     * joinAdmin.put("子表别名", "主表别名", Dict.create()
     * .set("子表字段名字A", "主表表字段名字A")
     * .set("子表字段名字B", "主表表字段名字B")
     * .set("子表字段名字C", "主表表字段名字C"));
     * joins.put("right", "s_admin", joinAdmin);
     * handleSQLJoin(sql , joins);
     * }
     * </pre>
     *
     * @param sql SQL语句
     *            * @param joins joins信息
     */
    static void handleSQLJoin(SQL sql, Table<String, String, Table<String, String, Dict>> joins) {
        if (Objects.nonNull(joins) && !joins.isEmpty()) {
            Map<String, Map<String, Table<String, String, Dict>>> conditionMap = joins.rowMap();
            conditionMap.forEach((joinType, joinInfo) -> joinInfo.forEach((subTableName, joinSpecification) -> handleJoinOnSpecification(sql, joinType, subTableName, joinSpecification)));
        }
    }

    /**
     * 处理JOIN后面的ON条件
     * <pre>
     * {@code
     * HashBasedTable<String, String, Dict> joinInfo = HashBasedTable.create();
     * joinInfo.put("子表别名", "主表别名", Dict.create()
     * .set("子表字段名字A", "主表表字段名字A")
     * .set("子表字段名字B", "主表表字段名字B")
     * .set("子表字段名字C", "主表表字段名字C"));
     * handleJoinOnSpecification(sql , "left" , "address" , joinInfo);
     * }
     * </pre>
     *
     * @param sql          SQL语句
     * @param joinType     链接类型
     * @param subTableName 子表名字(需要链接的表的名字)
     * @param join         JOIN信息
     */
    static void handleJoinOnSpecification(SQL sql, String joinType, String subTableName, Table<String, String, Dict> join) {
        if (Objects.nonNull(join) && !join.isEmpty()) {
            Map<String, Map<String, Dict>> rowMap = join.rowMap();
            String[] joinOnStr = new String[]{""};
            rowMap.forEach((subTableAlias, joinInfo) -> joinInfo.forEach((mainTableAlias, joinSpecification) -> {
                List<String> joinOnList = CollUtil.newArrayList();
                joinSpecification.forEach((subTableField, mainTableField) -> joinOnList.add(CharSequenceUtil.format("{}.{} = {}.{}", subTableAlias, subTableField, mainTableAlias, mainTableField)));
                joinOnStr[0] = CharSequenceUtil.format(GXBuilderConstant.JOIN_ON_STR, subTableName, subTableAlias, CollUtil.join(joinOnList, " and "));
            }));
            if (CharSequenceUtil.equalsIgnoreCase(GXBuilderConstant.LEFT_JOIN_TYPE, joinType)) {
                sql.LEFT_OUTER_JOIN(joinOnStr[0]);
            } else if (CharSequenceUtil.equalsIgnoreCase(GXBuilderConstant.RIGHT_JOIN_TYPE, joinType)) {
                sql.RIGHT_OUTER_JOIN(joinOnStr[0]);
            } else if (CharSequenceUtil.equalsIgnoreCase(GXBuilderConstant.INNER_JOIN_TYPE, joinType)) {
                sql.INNER_JOIN(joinOnStr[0]);
            }
        }
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
    static <R> String paginate(IPage<R> page, GXDBQueryParamInnerDto dbQueryParamInnerDto) {
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
    static String findOneByCondition(GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        GXValidatorUtils.validateEntity(dbQueryParamInnerDto);
        Table<String, String, Object> condition = dbQueryParamInnerDto.getCondition();
        Set<String> columns = dbQueryParamInnerDto.getColumns();
        String tableName = dbQueryParamInnerDto.getTableName();
        String tableNameAlias = Optional.ofNullable(dbQueryParamInnerDto.getTableNameAlias()).orElse(tableName);
        String selectStr = "*";
        if (CollUtil.isNotEmpty(columns)) {
            selectStr = String.join(",", columns);
        }
        SQL sql = new SQL().SELECT(selectStr).FROM(CharSequenceUtil.format("{} {}", tableName, tableNameAlias));
        handleSQLWhereCondition(sql, condition, tableNameAlias);
        sql.WHERE(CharSequenceUtil.format("{}.is_deleted = {}", tableNameAlias, getIsNotDeletedValue()));
        sql.LIMIT(1);
        return sql.toString();
    }

    /**
     * 处理SQL语句
     * <pre>
     * {@code
     * Table<String, String, Object> condition = HashBasedTable.create();
     * condition.put(GXAdminConstant.PRIMARY_KEY, GXBaseBuilderConstant.NUMBER_EQ, 22);
     * condition.put("created_at", GXBaseBuilderConstant.NUMBER_LE, 11111);
     * condition.put("created_at", GXBaseBuilderConstant.NUMBER_GE, 22222);
     * condition.put("username", GXBaseBuilderConstant.RIGHT_LIKE, "jetty");
     * HashBasedTable<String, String, Object> multiColumn = HashBasedTable.create();
     * multiColumn.put("username , '-' , nickname", GXBuilderConstant.RIGHT_LIKE, "77777");
     * multiColumn.put("username ,'-' , real_name", GXBuilderConstant.RIGHT_LIKE, "99999");
     * condition.put("T_FUNC" , "concat" , multiColumn);
     * condition.put("T_FUNC" , "JSON_OVERLAPS", "items->'$.zipcode', CAST('[94536]' AS JSON)");
     * condition.put("T_FUNC" , "JSON_OVERLAPS", CollUtil.newHashSet("items->'$.zipcode', CAST('[94536]' AS JSON)", "items->'$.zipcode1', CAST('[94537]' AS JSON)"));
     * handleSQLWhereCondition(sql , condition);
     * }
     * </pre>
     *
     * @param sql            SQL语句
     * @param condition      条件
     * @param tableNameAlias 表的别名
     */
    static void handleSQLWhereCondition(SQL sql, Table<String, String, Object> condition, String tableNameAlias) {
        if (Objects.nonNull(condition) && !condition.isEmpty()) {
            Map<String, Map<String, Object>> conditionMap = condition.rowMap();
            conditionMap.forEach((column, datum) -> {
                List<String> wheres = new ArrayList<>();
                datum.forEach((operator, value) -> {
                    if (Objects.nonNull(value)) {
                        String whereStr = "";
                        if (CharSequenceUtil.equalsIgnoreCase(GXBuilderConstant.T_FUNC_MARK, column)) {
                            if (value instanceof Table) {
                                Table<String, String, Object> table = Convert.convert(Table.class, value);
                                Map<String, Map<String, Object>> rowMap = table.rowMap();
                                rowMap.forEach((c, op) -> op.forEach((k, v) -> wheres.add(CharSequenceUtil.format(CharSequenceUtil.format("{} ({}) {} ", operator, c, k), GXSQLFilter.sqlInject(v.toString())))));
                            } else if (value instanceof Set) {
                                Set<String> stringSet = Convert.toSet(String.class, value);
                                stringSet.forEach(v -> wheres.add(CharSequenceUtil.format("{} ({}) ", operator, GXSQLFilter.sqlInject(v))));
                            } else {
                                whereStr = CharSequenceUtil.format("{} ({}) ", operator, GXSQLFilter.sqlInject(value.toString()));
                                wheres.add(whereStr);
                            }
                        } else {
                            whereStr = CharSequenceUtil.format("{} " + operator, handleWhereColumn(column, tableNameAlias), GXSQLFilter.sqlInject(value.toString()));
                            wheres.add(whereStr);
                        }
                    }
                });
                String whereStr = String.join(" AND ", wheres);
                sql.WHERE(whereStr);
            });
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
    static String deleteSoftWhere(String tableName, Table<String, String, Object> condition) {
        SQL sql = new SQL().UPDATE(tableName);
        sql.SET("is_deleted = id", CharSequenceUtil.format("deleted_at = {}", DateUtil.currentSeconds()));
        condition.put("is_deleted", GXBuilderConstant.NUMBER_EQ, getIsNotDeletedValue());
        handleSQLWhereCondition(sql, condition, "");
        return sql.toString();
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return SQL语句
     */
    static String deleteWhere(String tableName, Table<String, String, Object> condition) {
        SQL sql = new SQL().DELETE_FROM(tableName);
        handleSQLWhereCondition(sql, condition, "");
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
    default String getSelectField(String tableName, Set<String> targetSet, String tableAlias, boolean remove) {
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
    default String getSelectField(String tableName, Set<String> targetSet, boolean remove) {
        final GXDBSchemaService schemaService = GXSpringContextUtils.getBean(GXDBSchemaService.class);
        assert schemaService != null;
        return schemaService.getSelectFieldStr(tableName, targetSet, remove);
    }
}
