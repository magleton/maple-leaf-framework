package com.geoxus.core.framework.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.framework.entity.GXCoreAttributesEntity;
import com.geoxus.core.framework.service.GXAlterTableService;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.core.framework.service.GXDBSchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class GXAlterTableServiceImpl implements GXAlterTableService {
    private static final String ALTER_TABLE_HEADER = "ALTER TABLE `%s`\n";

    private static final String ADD_COLUMN_SQL = "ADD COLUMN `%s`%s AS (ext->>\"$.%s\") STORED NULL AFTER `%s`\n";

    private static final String DROP_COLUMN_SQL = "DROP COLUMN `%s`\n";

    private static final String CREATE_INDEX_SQL = "ADD INDEX `idx_%s`(%s) USING BTREE;";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private GXCoreAttributesService coreAttributesService;

    @Autowired
    private GXCoreModelService coreModelService;

    @Autowired
    private GXDBSchemaService gxdbSchemaService;

    @Override
    @Transactional
    public boolean addTableSearchCondition(String tableName, int coreModelId, String oldSearchCondition, String newSearchCondition) throws SQLException {
        final Map<String, Object> oldConditionMap = JSONUtil.toBean(oldSearchCondition, new TypeReference<Map<String, Object>>() {
        }, false);
        final Map<String, Object> newConditionMap = JSONUtil.toBean(newSearchCondition, new TypeReference<Map<String, Object>>() {
        }, false);
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            final String dropSQL = generateDropColumnSQL(oldConditionMap, tableName);
            if (!dropSQL.isEmpty()) {
                statement.execute(dropSQL);
            }
            final String addColumnSQL = generateAddColumnSQL(newConditionMap, tableName, coreModelId);
            if (!addColumnSQL.isEmpty()) {
                statement.execute(addColumnSQL);
            }
        }
        return true;
    }

    @Override
    public boolean tableCreateIndex(String tableName, int coreModelId, String indexFieldParam) throws SQLException {
        try {
            final Map<String, Object> indexData = JSONUtil.toBean(indexFieldParam, new TypeReference<Map<String, Object>>() {
            }, false);
            final String indexFieldStr = "`%s`";
            final String indexSubPart = "(%s)";
            final Set<String> keySet = indexData.keySet();
            if (!indexData.isEmpty()) {
                StringBuilder indexField = new StringBuilder();
                StringBuilder indexName = new StringBuilder();
                for (String key : keySet) {
                    indexName.append(CharSequenceUtil.format("_{}", key));
                    final int value = Integer.parseInt(indexData.get(key).toString());
                    indexField.append(String.format(indexFieldStr, key));
                    if (value > 0) {
                        indexField.append(String.format(indexSubPart, value));
                    }
                    indexField.append(",");
                }
                final String indexFormat = String.format(CREATE_INDEX_SQL, indexName, indexField.substring(0, indexField.lastIndexOf(",")));
                final String sql = String.format(ALTER_TABLE_HEADER, tableName) + indexFormat;
                try (final Connection connection = dataSource.getConnection();
                     final Statement statement = connection.createStatement()) {
                    statement.execute(sql);
                }
            }
            return true;
        } catch (JSONException e) {
            log.error("不合法的JSON数据", e);
        }
        return false;
    }

    @Override
    public List<GXDBSchemaService.TableField> getTableColumns(String tableName) {
        return gxdbSchemaService.getTableColumn(tableName);
    }

    @Override
    public List<GXDBSchemaService.TableIndexData> getTableIndexes(String tableName) throws SQLException {
        return gxdbSchemaService.listTableIndex(tableName);
    }

    @Override
    public boolean tableDropIndex(String tableName, String indexName) {
        return gxdbSchemaService.dropTableIndex(tableName, indexName);
    }

    @Override
    public boolean tableCheckIndexExists(String tableName, String indexName) {
        return gxdbSchemaService.checkTableIndexExists(tableName, indexName);
    }

    /**
     * 组合ADD Column SQL语句
     *
     * @param conditionMap 条件
     * @param tableName    表名
     * @param coreModelId  模型ID
     * @return String
     */
    private String generateAddColumnSQL(Map<String, Object> conditionMap, String tableName, int coreModelId) {
        final StringBuilder sql = new StringBuilder(String.format(ALTER_TABLE_HEADER, tableName));
        final Set<String> keySet = conditionMap.keySet();
        for (String key : keySet) {
            final String field = MapUtil.getStr(conditionMap, key);
            final GXCoreAttributesEntity attribute = coreAttributesService.getAttributeByAttributeName(field);
            if (coreModelService.checkModelHasAttribute(coreModelId, field) && !gxdbSchemaService.checkTableFieldExists(tableName, field)) {
                sql.append(String.format(ADD_COLUMN_SQL, field, attribute.getDataType(), field, "ext")).append(",");
            }
        }
        if (sql.lastIndexOf(",") > -1) {
            return sql.substring(0, sql.lastIndexOf(",")) + ";";
        }
        return "";
    }

    /**
     * 组合DROP Column SQL语句
     *
     * @param conditionMap 条件
     * @param tableName    表名
     * @return String
     */
    private String generateDropColumnSQL(Map<String, Object> conditionMap, String tableName) {
        final StringBuilder sql = new StringBuilder(String.format(ALTER_TABLE_HEADER, tableName));
        final Set<String> keySet = conditionMap.keySet();
        for (String key : keySet) {
            final String field = MapUtil.getStr(conditionMap, key);
            if (gxdbSchemaService.checkTableFieldExists(tableName, field)) {
                sql.append(String.format(DROP_COLUMN_SQL, field)).append(",");
            }
        }
        if (sql.lastIndexOf(",") > -1) {
            return sql.substring(0, sql.lastIndexOf(",")) + ";";
        }
        return "";
    }
}
