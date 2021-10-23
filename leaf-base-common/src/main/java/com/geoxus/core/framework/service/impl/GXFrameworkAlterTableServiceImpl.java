package com.geoxus.core.framework.service.impl;

import cn.hutool.core.map.MapUtil;
import com.geoxus.core.framework.entity.GXCoreAttributesEntity;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.core.service.GXAlterTableService;
import com.geoxus.core.service.impl.GXAlterTableServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Service
public class GXFrameworkAlterTableServiceImpl extends GXAlterTableServiceImpl implements GXAlterTableService {
    @Resource
    private GXCoreAttributesService coreAttributesService;

    @Resource
    private GXCoreModelService coreModelService;

    /**
     * 组合ADD Column SQL语句
     *
     * @param conditionMap 条件
     * @param tableName    表名
     * @param coreModelId  模型ID
     * @return String
     */
    public String generateAddColumnSQL(Map<String, Object> conditionMap, String tableName, int coreModelId) {
        final StringBuilder sql = new StringBuilder(String.format(ALTER_TABLE_HEADER, tableName));
        final Set<String> keySet = conditionMap.keySet();
        for (String key : keySet) {
            final String field = MapUtil.getStr(conditionMap, key);
            final GXCoreAttributesEntity attribute = coreAttributesService.getAttributeByAttributeName(field);
            if (coreModelService.checkModelHasAttribute(coreModelId, field) && !schemaService.checkTableFieldExists(tableName, field)) {
                sql.append(String.format(ADD_COLUMN_SQL, field, attribute.getDataType(), field, "ext")).append(",");
            }
        }
        if (sql.lastIndexOf(",") > -1) {
            return sql.substring(0, sql.lastIndexOf(",")) + ";";
        }
        return "";
    }
}
