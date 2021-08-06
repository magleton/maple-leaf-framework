package com.geoxus.core.common.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.validator.GXValidateModelMap;
import com.geoxus.core.common.vo.common.GXResultCode;
import com.geoxus.core.framework.entity.GXCoreAttributesEntity;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class GXValidateModelMapServiceImpl implements GXValidateModelMap {
    @Resource
    private GXCoreAttributesService coreAttributesService;

    @Resource
    private GXCoreModelService coreModelService;

    @Override
    public boolean validateModelMap(Dict map, String modelName) {
        final Set<String> keySet = map.keySet();
        if (!coreModelService.checkFormKeyMatch(keySet, modelName)) {
            return false;
        }
        for (String key : keySet) {
            final GXCoreAttributesEntity attributesEntity = coreAttributesService.getAttributeByAttributeName(map.getStr(key));
            final boolean matches = Pattern.matches(attributesEntity.getValidationExpression(), map.get(key).toString());
            if (!matches) {
                throw new GXException(GXResultCode.PARAMETER_VALIDATION_ERROR);
            }
        }
        return true;
    }
}
