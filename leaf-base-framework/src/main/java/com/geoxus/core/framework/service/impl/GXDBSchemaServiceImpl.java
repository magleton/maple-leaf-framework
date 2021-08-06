package com.geoxus.core.framework.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.framework.service.GXCoreModelAttributePermissionService;
import com.geoxus.core.framework.service.GXCoreModelAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.core.framework.service.GXDBSchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Service
public class GXDBSchemaServiceImpl implements GXDBSchemaService {
    @GXFieldCommentAnnotation(zhDesc = "获取索引SQL模板")
    private static final String SHOW_INDEX_SQL = "SHOW INDEX FROM `{}`";

    @GXFieldCommentAnnotation(zhDesc = "删除索引SQL模板")
    private static final String DROP_INDEX_SQL = "DROP INDEX `{}` on `{}`";

    @GXFieldCommentAnnotation(zhDesc = "数据源对象")
    @Autowired
    private DataSource dataSource;

    @GXFieldCommentAnnotation(zhDesc = "字段权限对象")
    @Autowired
    private GXCoreModelAttributePermissionService gxCoreModelAttributePermissionService;

    @Autowired
    private GXCoreModelService gxCoreModelService;

    @Autowired
    private GXCoreModelAttributesService gxCoreModelAttributesService;

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #tableName")
    public List<GXDBSchemaService.TableField> getTableColumn(String tableName) {
        final List<TableField> resultData = new ArrayList<>();
        try (final Connection connection = dataSource.getConnection()) {
            String databaseName = connection.getCatalog();
            final String sql = "SELECT column_name,data_type,column_type FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '{}' AND TABLE_NAME ='{}'";
            try (final Statement statement = connection.createStatement();
                 final ResultSet resultSet = statement.executeQuery(StrUtil.format(sql, databaseName, tableName))) {
                while (resultSet.next()) {
                    final TableField tableField = new TableField();
                    final String columnName = resultSet.getString("column_name");
                    final String dataType = resultSet.getString("data_type");
                    final String columnType = resultSet.getString("column_type");
                    tableField.setColumnName(columnName);
                    tableField.setColumnType(columnType);
                    tableField.setDataType(dataType);
                    resultData.add(tableField);
                }
            }
        } catch (SQLException e) {
            log.error("获取数据表的字段出错", e);
        }
        return resultData;
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName +#tableName")
    public List<GXDBSchemaService.TableIndexData> listTableIndex(String tableName) throws SQLException {
        Map<String, Map<String, Object>> returnList = new HashMap<>();
        List<GXDBSchemaService.TableIndexData> list = new ArrayList<>();
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(StrUtil.format(SHOW_INDEX_SQL, tableName))) {
            while (resultSet.next()) {
                final TableIndexData tableIndexData = new GXDBSchemaService.TableIndexData();
                tableIndexData.setNonUnique(resultSet.getInt("non_unique"));
                tableIndexData.setKeyName(resultSet.getString("key_name"));
                tableIndexData.setSeqInIndex(resultSet.getString("seq_in_index"));
                tableIndexData.setColumnName(resultSet.getString("column_name"));
                tableIndexData.setCardinality(resultSet.getInt("cardinality"));
                list.add(tableIndexData);
            }
        }
        for (GXDBSchemaService.TableIndexData data : list) {
            final Map<String, Object> map = Optional.ofNullable(returnList.get(data.getKeyName())).orElse(new HashMap<>());
            map.put(data.getSeqInIndex(), data.getColumnName());
            returnList.put(data.getKeyName(), map);
        }
        log.info("{}", returnList);
        return list;
    }

    @Override
    @CacheEvict(value = "table_column", key = "targetClass + methodName +#tableName")
    public boolean dropTableIndex(String tableName, String indexName) {
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement()) {
            if (checkTableFieldExists(tableName, indexName)) {
                return statement.execute(CharSequenceUtil.format(DROP_INDEX_SQL, indexName, tableName));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean checkTableFieldExists(String tableName, String field) {
        final List<TableField> tableFieldList = getTableColumn(tableName);
        for (TableField tableField : tableFieldList) {
            if (field.equals(tableField.getColumnName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkTableIndexExists(String tableName, String indexName) {
        String checkIndexExistsSQL = CharSequenceUtil.format(CharSequenceUtil.concat(true, SHOW_INDEX_SQL, " where key_name = '{}'"), tableName, indexName);
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(checkIndexExistsSQL)) {
            return resultSet.first();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #tableName + #targetSet")
    public String getSelectFieldStr(String tableName, Set<String> targetSet) {
        return getSelectFieldStr(tableName, targetSet, "", false);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #tableName + #targetSet")
    public String getSelectFieldStr(String tableName, Set<String> targetSet, boolean remove) {
        return getSelectFieldStr(tableName, targetSet, "", remove);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #tableName + #targetSet + #tableAlias")
    public String getSelectFieldStr(String tableName, Set<String> targetSet, String tableAlias, boolean remove) {
        return getSelectFieldStr(tableName, targetSet, tableAlias, remove, false);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #tableName + #targetSet + #tableAlias")
    public String getSelectFieldStr(String tableName, Set<String> targetSet, String tableAlias, boolean remove, boolean saveJSONField) {
        if (targetSet.size() == 1 && targetSet.contains("*")) {
            if (remove) {
                log.error("删除字段不能为'*' , 请指定需要删除的具体字段...");
            }
            return "*";
        }
        int coreModelId = gxCoreModelService.getCoreModelIdByTableName(tableName);
        final List<TableField> tableFields = getTableColumn(tableName);
        final Dict tmpResult = Dict.create();
        boolean allFieldFlag = targetSet.size() == 1 && targetSet.contains("*");
        for (TableField tableField : tableFields) {
            final String columnName = tableField.getColumnName();
            if (GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME.equals(columnName)) {
                tmpResult.set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, columnName);
                continue;
            }
            String dataType = tableField.getDataType();
            if (dataType.equalsIgnoreCase("json") && !saveJSONField) {
                if ((remove && targetSet.contains(columnName))) {
                    continue;
                }
                Dict attributeCondition = Dict.create().set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId).set("table_field_name", columnName);
                List<Dict> attributes = gxCoreModelAttributesService.getModelAttributesByModelId(attributeCondition);
                String attributeFlag = "attribute_name";
                if (attributes.isEmpty()) {
                    tmpResult.set(columnName, columnName);
                    continue;
                }
                for (Dict dict : attributes) {
                    String attributeValue = dict.getStr(attributeFlag);
                    attributeValue = CharSequenceUtil.toCamelCase(attributeValue);
                    String extFieldKey = CharSequenceUtil.format("{}::{}", columnName, attributeValue);
                    String lastAttributeName = CharSequenceUtil.format("{}->>'$.{}' as '{}'", columnName, attributeValue, extFieldKey);
                    if (allFieldFlag) {
                        tmpResult.set(extFieldKey, lastAttributeName);
                        continue;
                    }
                    if (remove) {
                        if (!targetSet.contains(extFieldKey)) {
                            tmpResult.set(extFieldKey, lastAttributeName);
                        }
                    } else {
                        if (targetSet.contains(extFieldKey) || targetSet.contains(columnName)) {
                            tmpResult.set(extFieldKey, lastAttributeName);
                        }
                    }
                }
            } else {
                if (allFieldFlag) {
                    tmpResult.set(columnName, columnName);
                    continue;
                }
                if (remove) {
                    if (!targetSet.contains(columnName)) {
                        tmpResult.set(columnName, columnName);
                    }
                } else {
                    if (targetSet.contains(columnName)) {
                        tmpResult.set(columnName, columnName);
                    }
                }
            }
        }
        final Dict permissions = gxCoreModelAttributePermissionService.getModelAttributePermissionByCoreModelId(coreModelId, Dict.create());
        Dict dict = Dict.create();
        if (!permissions.isEmpty() && null != permissions.getObj("db_field")) {
            dict = Convert.convert(Dict.class, permissions.getObj("db_field"));
        }
        final Set<String> permissionsKey = dict.keySet();
        final Set<String> result = new HashSet<>();
        for (Map.Entry<String, Object> entry : tmpResult.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (CollUtil.contains(permissionsKey, key)) {
                continue;
            }
            if (CharSequenceUtil.isBlank(tableAlias)) {
                result.add(CharSequenceUtil.format("{}", value));
            } else {
                result.add(CharSequenceUtil.format("{}.{}", tableAlias, value));
            }
        }
        return String.join(",", result);
    }
}
