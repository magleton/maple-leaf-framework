package com.geoxus.service.impl;

import com.geoxus.core.common.validator.GXValidateJsonFieldService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
@Primary
public class MyGXValidateJsonFieldServiceImpl implements GXValidateJsonFieldService {
    @Override
    public boolean validateJsonFieldData(Object o, String tableName, String parentFieldName, String fieldName, ConstraintValidatorContext context) throws UnsupportedOperationException {
        return true;
    }
}
