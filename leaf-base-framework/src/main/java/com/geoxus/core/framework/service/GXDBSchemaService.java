package com.geoxus.core.framework.service;

import lombok.Data;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface GXDBSchemaService {
    /**
     * 获取表的列
     *
     * @param tableName 数据表名
     * @return List
     */
    List<GXDBSchemaService.TableField> getTableColumn(String tableName);

    /**
     * 获取表的索引
     *
     * @param tableName 表名
     * @return List
     */
    List<GXDBSchemaService.TableIndexData> listTableIndex(String tableName) throws SQLException;

    /**
     * 删除表的索引
     *
     * @param tableName 表名
     * @param indexName 索引名
     * @return boolean
     */
    boolean dropTableIndex(String tableName, String indexName);

    /**
     * 检测表中是否有指定的字段
     *
     * @param tableName 数据表名字
     * @param field     字段名字
     * @return boolean
     */
    boolean checkTableFieldExists(String tableName, String field);

    /**
     * 检测索引是否存在
     *
     * @param tableName 数据表名字
     * @param indexName 索引名字
     * @return boolean
     */
    boolean checkTableIndexExists(String tableName, String indexName);

    /**
     * 获取SQL语句的查询字段
     *
     * @param tableName 数据表名
     * @param targetSet 字段集合
     * @return String
     */
    String getSelectFieldStr(String tableName, Set<String> targetSet);

    /**
     * 获取SQL语句的查询字段
     *
     * @param tableName 数据表名
     * @param targetSet 字段集合
     * @param remove    是否移除
     * @return String
     */
    String getSelectFieldStr(String tableName, Set<String> targetSet, boolean remove);

    /**
     * 获取SQL语句的查询字段
     *
     * @param tableName  数据表名
     * @param targetSet  字段集合
     * @param tableAlias 数据表的别名
     * @param remove     是否移除
     * @return String
     */
    String getSelectFieldStr(String tableName, Set<String> targetSet, String tableAlias, boolean remove);

    /**
     * 获取SQL语句的查询字段
     *
     * @param tableName     数据表名
     * @param targetSet     字段集合
     * @param tableAlias    数据表的别名
     * @param remove        是否移除
     * @param saveJSONField 是否保留JSON字段
     * @return String
     */
    String getSelectFieldStr(String tableName, Set<String> targetSet, String tableAlias, boolean remove, boolean saveJSONField);

    @Data
    class TableField {
        private String columnName;
        private String dataType;
        private String columnType;
    }

    @Data
    class TableIndexData {
        private int nonUnique;
        private String keyName;
        private String seqInIndex;
        private String columnName;
        private int cardinality;
    }
}
