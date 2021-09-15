package com.geoxus.core.common.validator.impl;

import com.geoxus.core.common.annotation.GXMergeSingleFieldAnnotation;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.common.validator.GXValidateJsonFieldService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GXValidateSingleJsonFieldValidator implements ConstraintValidator<GXMergeSingleFieldAnnotation, Object> {
    /**
     * 实际处理的服务
     */
    private GXValidateJsonFieldService validateJsonFieldService;

    /**
     * 表名字
     */
    private String tableName;

    /**
     * 数据库表中扩展字段中的字段名字
     * {"ext":{"author":"枫叶思源"}}
     */
    private String parentFieldName;

    private String fieldName;

    @Override
    public void initialize(GXMergeSingleFieldAnnotation annotation) {
        Class<? extends GXValidateJsonFieldService> serviceClazz = annotation.service();
        tableName = annotation.tableName();
        parentFieldName = annotation.parentFieldName();
        fieldName = annotation.fieldName();
        validateJsonFieldService = GXSpringContextUtils.getBean(serviceClazz);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        return validateJsonFieldService.validateJsonFieldData(o, tableName, parentFieldName, fieldName, context);
    }
}
