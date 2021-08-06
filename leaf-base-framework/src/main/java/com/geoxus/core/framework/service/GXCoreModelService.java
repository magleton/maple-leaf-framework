package com.geoxus.core.framework.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.validator.GXValidateDBExists;
import com.geoxus.core.framework.entity.GXCoreModelEntity;

import java.util.Set;

public interface GXCoreModelService extends GXBaseService<GXCoreModelEntity>, GXValidateDBExists {
    /**
     * 通过模型ID获取模型的相关信息
     *
     * @param modelId             模型ID
     * @param modelAttributeField 字段名字
     * @return GXCoreModelEntity
     */
    GXCoreModelEntity getCoreModelByModelId(int modelId, String modelAttributeField);

    /**
     * 检测模型是否拥有制定的字段
     *
     * @param modelId       核心模型ID
     * @param attributeName 　属性名字
     * @return boolean
     */
    boolean checkModelHasAttribute(int modelId, String attributeName);

    /**
     * 检测表单提交的key是否与模型的key匹配
     *
     * @param keySet    属性key集合
     * @param modelName 模型名字
     * @return boolean
     */
    boolean checkFormKeyMatch(Set<String> keySet, String modelName) throws GXException;

    /**
     * 通过模型名字获取模型ID
     *
     * @param modelName 模型名字
     * @return int
     */
    Integer getModelIdByModelIdentification(String modelName);

    /**
     * 通过modelId获取命名空间
     *
     * @param coreModelId  核心模型ID
     * @param defaultValue 默认值
     * @return String
     */
    String getModelTypeByModelId(long coreModelId, String defaultValue);

    /**
     * 获取模型配置的搜索条件
     *
     * @param condition 条件
     * @return Dict
     */
    Dict getSearchCondition(Dict condition);

    /**
     * 获取模型配置的搜索条件
     *
     * @param condition 条件
     * @return Dict
     */
    Dict getSearchCondition(Dict condition, String aliasPrefix);

    /**
     * 通过表名获取核心模型ID
     *
     * @param tableName 真实表名
     * @return int
     */
    int getCoreModelIdByTableName(String tableName);
}
