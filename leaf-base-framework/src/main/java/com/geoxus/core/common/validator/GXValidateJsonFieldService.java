package com.geoxus.core.common.validator;

import javax.validation.ConstraintValidatorContext;

/**
 * 验证模型的扩展数据
 */
public interface GXValidateJsonFieldService {
    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param o             The value to check for
     * @param tableName     The name of the model
     * @param jsonFieldName JSON Field Name
     * @param context       context
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean validateJsonFieldData(Object o, String tableName, String jsonFieldName, ConstraintValidatorContext context) throws UnsupportedOperationException;

    /**
     * 在DB中获取字段的值
     *
     * @param tableName 表名
     * @param fieldName 字段名字
     * @return Object
     */
    Object getFieldValueByCondition(String tableName, String fieldName);
}
