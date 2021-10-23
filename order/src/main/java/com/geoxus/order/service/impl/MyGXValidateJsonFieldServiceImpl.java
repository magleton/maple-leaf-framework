package com.geoxus.order.service.impl;

import com.geoxus.core.framework.validator.GXValidateJSONFieldService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
public class MyGXValidateJsonFieldServiceImpl implements GXValidateJSONFieldService {
    @Override
    public boolean validateJsonFieldData(Object o, String tableName, String parentFieldName, String fieldName, ConstraintValidatorContext context) throws UnsupportedOperationException {
        return true;
    }
}
