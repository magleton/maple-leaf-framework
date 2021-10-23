package com.geoxus.common.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.framework.validator.GXValidateJSONFieldService;
import com.geoxus.core.datasource.annotation.GXDataSource;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
@GXDataSource("framework")
public class GXValidateJsonFieldServiceImpl implements GXValidateJSONFieldService {
    @Override
    public boolean validateJsonFieldData(Object o, String tableName, String parentFieldName, String fieldName, ConstraintValidatorContext context) throws UnsupportedOperationException {
        return true;
    }

    @Override
    public Object getFieldValueByCondition(String tableName, String parentFieldName, String fieldName) {
        return CharSequenceUtil.format("{}-{}-{}", tableName, fieldName);
    }
}
