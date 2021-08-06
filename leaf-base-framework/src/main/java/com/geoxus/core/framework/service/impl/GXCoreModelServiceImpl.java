package com.geoxus.core.framework.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.framework.entity.GXCoreModelEntity;
import com.geoxus.core.framework.mapper.GXCoreModelMapper;
import com.geoxus.core.framework.service.GXCoreModelAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.util.*;

@Service
@Slf4j
@GXDataSourceAnnotation("framework")
public class GXCoreModelServiceImpl extends ServiceImpl<GXCoreModelMapper, GXCoreModelEntity> implements GXCoreModelService {
    @Autowired
    private GXCoreModelAttributesService coreModelAttributeService;

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #modelId + #modelAttributeField")
    public GXCoreModelEntity getCoreModelByModelId(int modelId, String modelAttributeField) {
        final GXCoreModelEntity entity = getById(modelId);
        if (null == entity) {
            return null;
        }
        if (CharSequenceUtil.isBlank(modelAttributeField)) {
            modelAttributeField = "";
        }
        final List<Dict> attributes = coreModelAttributeService.getModelAttributesByModelId(Dict.create().set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, modelId).set("db_field_name", modelAttributeField));
        entity.setCoreAttributes(attributes);
        return entity;
    }

    @Override
    public boolean checkModelHasAttribute(int modelId, String attributeName) {
        return 1 == coreModelAttributeService.checkCoreModelHasAttribute(modelId, attributeName);
    }

    @Override
    public boolean checkFormKeyMatch(Set<String> keySet, String modelName) {
        final GXCoreModelEntity modelEntity = getCoreModelByModelId(getModelIdByModelIdentification(modelName), null);
        if (null == modelEntity) {
            return false;
        }
        final List<Dict> attributes = modelEntity.getCoreAttributes();
        final Set<String> keys = new HashSet<>();
        for (Dict dict : attributes) {
            keys.add(dict.getStr("field_name"));
        }
        return keys.retainAll(keySet);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #p0")
    public Integer getModelIdByModelIdentification(String modelName) {
        final Dict condition = Dict.create().set(GXBaseBuilderConstants.MODEL_IDENTIFICATION_NAME, modelName);
        HashSet<String> field = CollUtil.newHashSet("model_id");
        final Dict dict = getFieldValueBySQL(GXCoreModelEntity.class, field, condition, false);
        return null == dict ? 0 : dict.getInt("model_id");
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #coreModelId")
    public String getModelTypeByModelId(long coreModelId, String defaultValue) {
        Dict condition = Dict.create().set("model_id", coreModelId);
        HashSet<String> field = CollUtil.newHashSet("model_identification");
        Dict dict = getFieldValueBySQL(GXCoreModelEntity.class, field, condition, false);
        String modelType = dict.getStr("model_identification");
        if (CharSequenceUtil.isBlank(modelType)) {
            return defaultValue;
        }
        return CharSequenceUtil.upperFirst(CharSequenceUtil.toCamelCase(modelType)) + "Type";
    }

    @Override
    public Dict getSearchCondition(Dict condition) {
        final Dict searchCondition = baseMapper.getSearchCondition(condition);
        if (null == searchCondition) {
            return Dict.create();
        }
        final String searchConditionName = CharSequenceUtil.toUnderlineCase(GXBaseBuilderConstants.SEARCH_CONDITION_NAME);
        return Convert.convert(new TypeReference<Dict>() {
        }, JSONUtil.parse(Optional.ofNullable(searchCondition.getObj(searchConditionName)).orElse("{}")));
    }

    @Override
    public Dict getSearchCondition(Dict condition, String aliasPrefix) {
        final Dict searchFields = getSearchCondition(condition);
        if (CharSequenceUtil.isBlank(aliasPrefix)) {
            return searchFields;
        }
        final Set<Map.Entry<String, Object>> entrySet = searchFields.entrySet();
        final Dict dict = Dict.create();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            final Object value = entry.getValue();
            if (!CharSequenceUtil.contains(key, '.')) {
                key = CharSequenceUtil.format("{}.{}", aliasPrefix, key);
            }
            dict.set(key, value);
        }
        return dict;
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #tableName")
    public int getCoreModelIdByTableName(String tableName) {
        final Dict condition = Dict.create().set("table_name", tableName);
        final QueryWrapper<GXCoreModelEntity> queryWrapper = new QueryWrapper<GXCoreModelEntity>().select("model_id").allEq(condition);
        Map<String, Object> data = getMap(queryWrapper);
        if (null == data || data.isEmpty()) {
            return 0;
        }
        return Convert.convert(Integer.class, data.get("model_id"));
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #value + #field")
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        log.info("validateExists : {} , field : {}", value, field);
        final Integer coreModelId = Convert.toInt(value, 0);
        final Object o = checkRecordIsExists(GXCoreModelEntity.class, Dict.create().set("model_id", coreModelId));
        return 1 == Convert.convert(Integer.class, o, 0);
    }
}
