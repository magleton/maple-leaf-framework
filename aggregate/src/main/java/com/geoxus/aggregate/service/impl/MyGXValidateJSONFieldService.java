package com.geoxus.aggregate.service.impl;

import com.geoxus.core.framework.properties.GXCommonProperties;
import com.geoxus.core.framework.validator.GXValidateJSONFieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;

@Service
public class MyGXValidateJSONFieldService implements GXValidateJSONFieldService {
    @Resource
    private GXCommonProperties commonProperties;

    @Override
    public boolean validateJsonFieldData(Object o, String tableName, String parentFieldName, String fieldName, ConstraintValidatorContext context) throws UnsupportedOperationException {
        System.out.println(fieldName + " --- Hello");
        return true;
    }
}
