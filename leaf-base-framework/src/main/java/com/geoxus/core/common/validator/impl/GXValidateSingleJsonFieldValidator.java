package com.geoxus.core.common.validator.impl;

import com.geoxus.core.common.annotation.GXSingleFieldToDbJsonFieldAnnotation;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.common.validator.GXValidateJsonFieldService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GXValidateSingleJsonFieldValidator implements ConstraintValidator<GXSingleFieldToDbJsonFieldAnnotation, Object> {
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
    private String dbJsonFieldName;

    @Override
    public void initialize(GXSingleFieldToDbJsonFieldAnnotation annotation) {
        Class<? extends GXValidateJsonFieldService> serviceClazz = annotation.service();
        String tableName = annotation.tableName();
        String dbJSONFieldName = annotation.dbJSONFieldName();
        validateJsonFieldService = GXSpringContextUtils.getBean(serviceClazz);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        return validateJsonFieldService.validateJsonFieldData(o, tableName, dbJsonFieldName, context);
    }
}
