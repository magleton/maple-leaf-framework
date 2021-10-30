package cn.maple.core.framework.validator.impl;

import cn.maple.core.framework.annotation.GXValidateExtData;
import cn.maple.core.framework.util.GXSpringContextUtil;
import cn.maple.core.framework.validator.GXValidateExtDataService;

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
