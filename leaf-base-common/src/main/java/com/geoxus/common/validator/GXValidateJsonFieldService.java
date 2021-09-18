package com.geoxus.common.validator;

import javax.validation.ConstraintValidatorContext;

/**
 * 验证模型的扩展数据
 */
public interface GXValidateJsonFieldService {
    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param o               The value to check for
     * @param tableName       The name of the model
     * @param parentFieldName JSON Field Name
     * @param fieldName       fieldName
     * @param context         context
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    default boolean validateJsonFieldData(Object o, String tableName, String parentFieldName, String fieldName, ConstraintValidatorContext context) throws UnsupportedOperationException {
        return true;
    }

    /**
     * 在DB中获取字段的值
     *
     * @param tableName       表名
     * @param parentFieldName 父级字段名字
     * @param fieldName       字段名字
     * @return Object
     */
    default Object getFieldValueByCondition(String tableName, String parentFieldName, String fieldName) {
        return null;
    }
}
