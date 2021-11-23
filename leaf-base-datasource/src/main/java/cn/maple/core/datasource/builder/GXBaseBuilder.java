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
import cn.maple.core.datasource.constant.GXBaseBuilderConstant;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.service.GXDBSchemaService;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
                sql.SET(CharSequenceUtil.format("{} " + GXBaseBuilderConstant.NUMBER_EQ, fieldName, value));
            } else {
                sql.SET(CharSequenceUtil.format("{} " + GXBaseBuilderConstant.STR_EQ, fieldName, value));
            }
        }
        sql.SET(CharSequenceUtil.format("updated_at = {}", DateUtil.currentSeconds()));
        dealSQLWhereCondition(sql, condition);
        return sql.toString();
    }

    /**
     * 判断给定条件的值是否存在
     *
     * @param tableName 表名
     * @param condition 条件
     * @return String
     */
    static String checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        final SQL sql = new SQL().SELECT("1").FROM(tableName);
        dealSQLWhereCondition(sql, condition);
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
    static String checkRecordIsUnique(String tableName, Table<String, String, Object> condition) {
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
    static String batchInsert(String tableName, Set<String> fieldSet, List<Dict> dataList) {
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
     * 通过条件获取数据列表
     *
     * @param tableName 表名字
     * @param fieldSet  需要查询的字段
     * @param condition 条件
     *                  {@code
     *                  Table<String, String, Object> condition = HashBasedTable.create();
     *                  condition.put("path" , "like" , "aaa%");
     *                  condition.put("path" , "in" , "(1,2,3,4,5,6)");
     *                  condition.put("level" , "=" , "1111");
     *                  findByCondition("test" , condition, CollUtil.newHashSet("id" , "username"));
     *                  Table<String, String, Object> condition = HashBasedTable.create();
     *                  condition.put("T_FUNC" , "JSON_OVERLAPS" , "items->'$.zipcode', CAST('[94536]' AS JSON)");
     *                  findByCondition("test", condition , CollUtil.newHashSet("id" , "username"));
     *                  }
     * @return SQL语句
     */
    static String findByCondition(String tableName, Table<String, String, Object> condition, Set<String> fieldSet) {
        String selectStr = "*";
        if (CollUtil.isNotEmpty(fieldSet)) {
            selectStr = String.join(",", fieldSet);
        }
        SQL sql = new SQL().SELECT(selectStr).FROM(tableName);
        dealSQLWhereCondition(sql, condition);
        sql.WHERE(CharSequenceUtil.format("is_deleted = {}", GXCommonConstant.NOT_DELETED_MARK));
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
     *                  findByCondition(Page , "test" , condition, CollUtil.newHashSet("id" , "username"));
     *                  condition1.put("T_FUNC" , "JSON_OVERLAPS" , "items->'$.zipcode', CAST('[94536]' AS JSON)");
     *                  findByCondition("test" , condition1, CollUtil.newHashSet("id" , "username"));
     *                  </code>
     * @return SQL语句
     */
    static <R> String paginate(IPage<R> page, String tableName, Table<String, String, Object> condition, Set<String> fieldSet) {
        return findByCondition(tableName, condition, fieldSet);
    }

    /**
     * 通过条件获取数据列表
     *
     * @param tableName 表名字
     * @param fieldSet  需要查询的字段
     * @param condition 条件
     *                  <code>
     *                  Table<String, String, Object> condition = HashBasedTable.create();
     *                  condition.put("path" , "like" , "aaa%");
     *                  condition.put("path" , "in" , "(1,2,3,4,5,6)");
     *                  condition.put("level" , "=" , "1111");
     *                  findByCondition("test" , condition, CollUtil.newHashSet("id" , "username"));
     *                  Table<String, String, Object> condition = HashBasedTable.create();
     *                  condition.put("T_FUNC" , "JSON_OVERLAPS" , "items->'$.zipcode', CAST('[94536]' AS JSON)");
     *                  findByCondition("test", condition , CollUtil.newHashSet("id" , "username"));
     *                  </code>
     * @return SQL语句
     */
    static String findOneByCondition(String tableName, Table<String, String, Object> condition, Set<String> fieldSet) {
        String selectStr = "*";
        if (CollUtil.isNotEmpty(fieldSet)) {
            selectStr = String.join(",", fieldSet);
        }
        SQL sql = new SQL().SELECT(selectStr).FROM(tableName);
        dealSQLWhereCondition(sql, condition);
        sql.WHERE(CharSequenceUtil.format("is_deleted = {}", GXCommonConstant.NOT_DELETED_MARK));
        sql.LIMIT(1);
        return sql.toString();
    }

    /**
     * 处理SQL语句
     * {@code
     * Table<String, String, Object> condition = HashBasedTable.create();
     * condition.put(GXAdminConstant.PRIMARY_KEY, GXBaseBuilderConstant.NUMBER_EQ, 22);
     * condition.put("created_at", GXBaseBuilderConstant.NUMBER_LE, 11111);
     * condition.put("created_at", GXBaseBuilderConstant.NUMBER_GE, 22222);
     * condition.put("username", GXBaseBuilderConstant.RIGHT_LIKE, "jetty");
     * dealSQLWhereCondition(sql , condition);
     * }
     *
     * @param sql       SQL语句
     * @param condition 条件
     */
    static void dealSQLWhereCondition(SQL sql, Table<String, String, Object> condition) {
        if (Objects.nonNull(condition)) {
            Map<String, Map<String, Object>> conditionMap = condition.rowMap();
            conditionMap.forEach((column, datum) -> {
                List<String> wheres = new ArrayList<>();
                datum.forEach((operator, value) -> {
                    if (Objects.nonNull(value)) {
                        String whereStr = "";
                        if (CharSequenceUtil.equalsIgnoreCase(GXBaseBuilderConstant.T_FUNC_MARK, column)) {
                            whereStr = CharSequenceUtil.format("{} ({}) ", operator, value);
                        } else {
                            //whereStr = CharSequenceUtil.format("{} {} {} ", column, operator, value);
                            whereStr = CharSequenceUtil.format("{} " + operator, column, value);
                        }
                        wheres.add(whereStr);
                        //wheres.add("or");
                    }
                });
                wheres.remove(wheres.size() - 1);
                String whereStr = String.join(" ", wheres);
                sql.WHERE(whereStr);
            });
        }
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
        sql.SET("is_deleted = id");
        dealSQLWhereCondition(sql, condition);
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
        dealSQLWhereCondition(sql, condition);
        return sql.toString();
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
