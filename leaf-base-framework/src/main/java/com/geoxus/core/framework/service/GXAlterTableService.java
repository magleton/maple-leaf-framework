package com.geoxus.core.framework.service;

import java.sql.SQLException;
import java.util.List;

public interface GXAlterTableService {
    /**
     * 新增搜索条件
     *
     * @param tableName          表名
     * @param coreModelId        模型ID
     * @param oldSearchCondition 旧的条件
     * @param newSearchCondition 新的条件
     * @return boolean
     */
    boolean addTableSearchCondition(String tableName, int coreModelId, String oldSearchCondition, String newSearchCondition) throws SQLException;

    /**
     * 为表创建索引
     *
     * @param tableName       表名
     * @param coreModelId     模型ID
     * @param indexFieldParam 索引字段
     * @return boolean
     */
    boolean tableCreateIndex(String tableName, int coreModelId, String indexFieldParam) throws SQLException;

    /**
     * 获取表的列
     *
     * @param tableName 表名
     * @return List
     */
    List<GXDBSchemaService.TableField> getTableColumns(String tableName) throws SQLException;

    /**
     * 列出数据表的索引
     *
     * @param tableName 表名
     * @return List
     */
    List<GXDBSchemaService.TableIndexData> getTableIndexes(String tableName) throws SQLException;

    /**
     * 删除数据表的索引
     *
     * @param tableName 表名
     * @param indexName 索引名字
     * @return boolean
     */
    boolean tableDropIndex(String tableName, String indexName) throws SQLException;

    /**
     * 检测索引是否存在
     *
     * @param tableName 表名
     * @param indexName 索引名字
     * @return boolean
     */
    boolean tableCheckIndexExists(String tableName, String indexName);
}
