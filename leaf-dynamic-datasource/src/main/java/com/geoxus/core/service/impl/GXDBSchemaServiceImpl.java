package com.geoxus.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.core.service.GXDBSchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Service
public class GXDBSchemaServiceImpl implements GXDBSchemaService {
    @GXFieldComment(zhDesc = "获取索引SQL模板")
    private static final String SHOW_INDEX_SQL = "SHOW INDEX FROM `{}`";

    @GXFieldComment(zhDesc = "删除索引SQL模板")
    private static final String DROP_INDEX_SQL = "DROP INDEX `{}` on `{}`";

    @GXFieldComment(zhDesc = "数据源对象")
    @Resource
    private DataSource dataSource;

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
    public String getSelectFieldStr(String tableName, Set<String> targetSet, String tableAlias, boolean remove, boolean saveJSONField) {
        if (targetSet.size() == 1 && targetSet.contains("*")) {
            if (remove) {
                log.error("删除字段不能为'*' , 请指定需要删除的具体字段...");
            }
            return "*";
        }
        if(!remove){
            return String.join(",", targetSet);
        }
        final Set<String> result = new HashSet<>();
        final List<TableField> tableFields = getTableColumn(tableName);
        for (TableField tableField : tableFields) {
            final String columnName = tableField.getColumnName();
            if(CollUtil.contains(targetSet , columnName)){
                continue;
            }
            result.add(columnName);
        }
        return String.join(",", result);
    }
}
