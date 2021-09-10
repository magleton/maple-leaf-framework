package com.geoxus.core.framework.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.validator.GXValidateJSONFieldService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
public class GXValidateJSONFieldServiceImpl implements GXValidateJSONFieldService {
    @Override
    public boolean validateJSONFieldData(Object o, String modelIdentification, String tableField, boolean isFullMatchAttribute, ConstraintValidatorContext context) throws UnsupportedOperationException {
        return false;
    }

    @Override
    public Object getFieldValueByCondition(String tableName, String fieldName) {
        return CharSequenceUtil.format("{}-{}-{}", tableName, fieldName);
    }
}
