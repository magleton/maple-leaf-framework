package com.geoxus.common.validator.impl;

import com.geoxus.common.annotation.GXValidateExtData;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.common.validator.GXValidateExtDataService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class GXValidateExtDataValidator implements ConstraintValidator<GXValidateExtData, Object> {
    private GXValidateExtDataService service;

    private String modelName;

    private String subField;

    private boolean isFullMatchAttribute;

    @Override
    public void initialize(GXValidateExtData constraintAnnotation) {
        Class<? extends GXValidateExtDataService> clazz = constraintAnnotation.service();
        modelName = constraintAnnotation.tableName();
        subField = constraintAnnotation.fieldName();
        service = GXSpringContextUtil.getBean(clazz);
        isFullMatchAttribute = constraintAnnotation.isFullMatchAttribute();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        if (null == o || Objects.isNull(service)) {
            return true;
        }
        return service.validateExtData(o, modelName, subField, isFullMatchAttribute, context);
    }
}
