package com.geoxus.core.framework.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.common.validator.GXValidateJSONFieldService;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
@GXDataSourceAnnotation("framework")
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
