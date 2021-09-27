package com.geoxus.core.framework.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.core.datasource.annotation.GXDataSource;
import com.geoxus.core.framework.constant.GXFrameWorkCommonConstant;
import com.geoxus.core.framework.dao.GXCoreModelAttributesDao;
import com.geoxus.core.framework.entity.GXCoreModelAttributesEntity;
import com.geoxus.core.framework.mapper.GXCoreModelAttributesMapper;
import com.geoxus.core.framework.service.GXCoreModelAttributesService;
import com.geoxus.core.framework.service.GXCoreModelTableFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
@GXDataSource("framework")
public class GXCoreModelAttributesServiceImpl extends GXBaseServiceImpl<GXCoreModelAttributesEntity, GXCoreModelAttributesMapper, GXCoreModelAttributesDao> implements GXCoreModelAttributesService {
    @Resource
    private GXCoreModelTableFieldService coreModelTableFieldService;

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #p0.getStr('core_model_id') + #p0.getStr('table_field_name')")
    public List<Dict> getModelAttributesByModelId(Dict param) {
        if (null == param.getInt(GXFrameWorkCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME)) {
            param.set(GXFrameWorkCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME, 0);
        }
        return getBaseMapper().getModelAttributesByCondition(param);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #modelId + #attributeId")
    public Dict getModelAttributeByModelIdAndAttributeId(int modelId, int attributeId) {
        final Dict condition = Dict.create().set(GXFrameWorkCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME, modelId).set("attribute_id", attributeId);
        final HashSet<String> fieldSet = CollUtil.newHashSet("validation_expression", "force_validation", "required");
        return getFieldValueBySQL(GXCoreModelAttributesEntity.class, fieldSet, condition, false);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #coreModelId + #attributeName")
    public Integer checkCoreModelHasAttribute(Integer coreModelId, String attributeName) {
        final Dict condition = Dict.create().set(GXFrameWorkCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId).set("attribute_name", attributeName);
        return getBaseMapper().checkCoreModelHasAttribute(condition);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #coreModelId + #modelAttributeField")
    public boolean checkCoreModelFieldAttributes(Integer coreModelId, String modelAttributeField, String jsonStr) {
        Set<String> paramSet = CollUtil.newHashSet();
        final Dict paramData = JSONUtil.toBean(jsonStr, Dict.class);
        if (paramData.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, Object> entry : paramData.entrySet()) {
            paramSet.add(entry.getKey());
        }
        final Dict condition = Dict.create().set("core_model_id", coreModelId).set("table_field_name", modelAttributeField);
        List<Dict> list = coreModelTableFieldService.getModelAttributesByModelId(condition);
        if (list.isEmpty()) {
            return false;
        }
        Set<String> dbSet = CollUtil.newHashSet();
        for (Dict dict : list) {
            if (null != dict.getStr("attribute_name")) {
                dbSet.add(dict.getStr("attribute_name"));
            }
        }
        log.info("checkCoreModelFieldAttributes ->> dbSet : {} , paramSet : {}", dbSet, paramSet);
        return dbSet.toString().equals(paramSet.toString());
    }

    @Override
    @SuppressWarnings("all")
    public Dict getModelAttributesDefaultValue(int coreModelId, String tableField, String jsonStr) {
        if (!JSONUtil.isJson(jsonStr)) {
            return Dict.create();
        }
        final Dict sourceDict = JSONUtil.toBean(jsonStr, Dict.class);
        final Dict condition = Dict.create()
                .set(GXFrameWorkCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId)
                .set("table_field_name", tableField);
        final Dict retDict = Dict.create();
        final List<Dict> list = getModelAttributesByCondition(condition);
        Dict errorsDict = Dict.create();
        for (Dict dict : list) {
            final String attributeName = dict.getStr("attribute_name");
            final String cmExt = dict.getStr("cm_ext");
            final String cExt = dict.getStr("c_ext");
            // 特定模型中的属性的元数据
            Dict cmExtDict = Dict.create();
            // 属性公共的元数据
            Dict cExtDict = Dict.create();
            if (JSONUtil.isJson(cmExt)) {
                cmExtDict = JSONUtil.toBean(cmExt, Dict.class);
            }
            if (JSONUtil.isJson(cExt)) {
                cExtDict = JSONUtil.toBean(cExt, Dict.class);
            }
            cExtDict.putAll(cmExtDict);
            if (!cExtDict.isEmpty()) {
                // 根据属性的特定元数据处理不同的情况, 此处可以派发事件 , 供扩展使用
            }
            int required = Optional.ofNullable(dict.getInt("required")).orElse(0);
            String errorTips = dict.getStr("error_tips");
            if (CharSequenceUtil.isBlank(errorTips)) {
                errorTips = CharSequenceUtil.format("{}.{}为必填字段", tableField, attributeName);
            }
            Object sourceValue = Optional.ofNullable(sourceDict.getObj(attributeName)).orElse(sourceDict.getObj(CharSequenceUtil.toCamelCase(attributeName)));
            Object defaultValue = dict.getObj("default_value");
            int isAutoGeneration = Optional.ofNullable(dict.getInt("is_auto_generation")).orElse(0);
            if (Objects.isNull(sourceValue) && Objects.isNull(defaultValue) && isAutoGeneration == 1) {
                defaultValue = RandomUtil.randomString(5);
            }
            if (required == 1 && Objects.isNull(sourceValue) && Objects.isNull(defaultValue)) {
                errorsDict.set(attributeName, errorTips);
                continue;
            }
            if (Objects.isNull(sourceValue)) {
                sourceValue = defaultValue;
            }
            Object value = Optional.ofNullable(sourceValue).orElse(dict.getObj("fixed_value"));
            retDict.set(attributeName, value);
        }
        if (!errorsDict.isEmpty()) {
            throw new GXBusinessException("属性必填错误", HttpStatus.HTTP_INTERNAL_ERROR, errorsDict);
        }
        return retDict;
    }

    /**
     * 通过条件获取模型的属性列表
     *
     * @param condition 条件
     * @return List
     */
    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #p0.getStr('core_model_id') + #p0.getStr('table_field_name')")
    public List<Dict> getModelAttributesByCondition(Dict condition) {
        return getBaseMapper().getModelAttributesByCondition(condition);
    }
}
