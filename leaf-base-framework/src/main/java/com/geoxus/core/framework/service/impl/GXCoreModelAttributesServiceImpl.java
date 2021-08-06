package com.geoxus.core.framework.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.util.GXCacheKeysUtils;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.framework.entity.GXCoreModelAttributesEntity;
import com.geoxus.core.framework.mapper.GXCoreModelAttributesMapper;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import com.geoxus.core.framework.service.GXCoreModelAttributesService;
import com.geoxus.core.framework.service.GXCoreModelTableFieldService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@GXDataSourceAnnotation("framework")
public class GXCoreModelAttributesServiceImpl extends ServiceImpl<GXCoreModelAttributesMapper, GXCoreModelAttributesEntity> implements GXCoreModelAttributesService {
    @GXFieldCommentAnnotation(zhDesc = "Guava缓存组件")
    private static final Cache<String, List<Dict>> LIST_DICT_CACHE;

    static {
        LIST_DICT_CACHE = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofHours(24)).maximumSize(100000).build();
    }

    @Resource
    private GXCacheKeysUtils gxCacheKeysUtils;

    @Resource
    private GXCoreModelTableFieldService gxCoreModelTableFieldService;

    @Resource
    private GXCoreAttributesService gxCoreAttributesService;

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #p0.getStr('core_model_id') + #p0.getStr('table_field_name')")
    public List<Dict> getModelAttributesByModelId(Dict param) {
        if (null == param.getInt(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME)) {
            param.set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, 0);
        }
        return baseMapper.getModelAttributesByModelId(param);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #modelId + #attributeId")
    public Dict getModelAttributeByModelIdAndAttributeId(int modelId, int attributeId) {
        final Dict condition = Dict.create().set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, modelId).set("attribute_id", attributeId);
        final HashSet<String> fieldSet = CollUtil.newHashSet("validation_expression", "force_validation", "required");
        return getFieldValueBySQL(GXCoreModelAttributesEntity.class, fieldSet, condition, false);
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #coreModelId + #attributeName")
    public Integer checkCoreModelHasAttribute(Integer coreModelId, String attributeName) {
        final Dict condition = Dict.create().set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId).set("attribute_name", attributeName);
        return baseMapper.checkCoreModelHasAttribute(condition);
    }

    @Override
    public boolean checkCoreModelFieldAttributes(Integer coreModelId, String modelAttributeField, String jsonStr) {
        Set<String> paramSet = CollUtil.newHashSet();
        final Dict paramData = JSONUtil.toBean(jsonStr, Dict.class);
        if (paramData.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, Object> entry : paramData.entrySet()) {
            paramSet.add(entry.getKey());
        }
        final String cacheKey = gxCacheKeysUtils.getCacheKey("", CharSequenceUtil.format("{}.{}.{}", coreModelId, modelAttributeField, String.join(".", paramSet)));
        final Dict condition = Dict.create().set("core_model_id", coreModelId).set("table_field_name", modelAttributeField);
        try {
            final List<Dict> list = LIST_DICT_CACHE.get(cacheKey, () -> gxCoreModelTableFieldService.getModelAttributesByModelId(condition));
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
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Dict getModelAttributesDefaultValue(int coreModelId, String tableField, String jsonStr) {
        if (!JSONUtil.isJson(jsonStr)) {
            return Dict.create();
        }
        final Dict sourceDict = JSONUtil.toBean(jsonStr, Dict.class);
        final String cacheKey = gxCacheKeysUtils.getCacheKey("", CharSequenceUtil.format("{}.{}", coreModelId, tableField));
        final Dict condition = Dict.create()
                .set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId)
                .set("table_field_name", tableField);
        try {
            final Dict retDict = Dict.create();
            final List<Dict> list = LIST_DICT_CACHE.get(cacheKey, () -> baseMapper.getModelAttributesByModelId(condition));
            Dict errorsDict = Dict.create();
            for (Dict dict : list) {
                final String attributeName = CharSequenceUtil.toCamelCase(dict.getStr("attribute_name"));
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
                    // 根据属性的特定元数据处理不同的情况
                }
                Integer required = dict.getInt("required");
                String errorTips = dict.getStr("error_tips");
                if (CharSequenceUtil.isBlank(errorTips)) {
                    errorTips = CharSequenceUtil.format("{}.{}为必填字段", tableField, attributeName);
                }
                if (required == 1 && null == sourceDict.getObj(attributeName) && null == dict.getObj("default_value")) {
                    errorsDict.set(attributeName, errorTips);
                    continue;
                }
                Object value = dict.getStr("fixed_value");
                if (StrUtil.isBlankIfStr(value)) {
                    value = sourceDict.getObj(attributeName);
                    if (StrUtil.isBlankIfStr(value) || GXCommonUtils.getClassDefaultValue(sourceDict.getObj(attributeName).getClass()).equals(value)) {
                        value = dict.getObj("default_value");
                        if (StrUtil.isBlankIfStr(value) || GXCommonUtils.getClassDefaultValue(sourceDict.getObj(attributeName).getClass()).equals(value)) {
                            if (null != dict.getObj("is_auto_generation") && dict.getInt("is_auto_generation") == 1) {
                                value = RandomUtil.randomString(5);
                            } else {
                                errorsDict.set(attributeName, errorTips);
                                continue;
                            }
                        }
                    }
                }
                retDict.set(attributeName, value);
            }
            if (!errorsDict.isEmpty()) {
                throw new GXException("属性必填错误", HttpStatus.HTTP_INTERNAL_ERROR, errorsDict);
            }
            return retDict;
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
        }
        return Dict.create();
    }
}
