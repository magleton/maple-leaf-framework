package cn.maple.core.datasource.service;

import cn.hutool.core.lang.Dict;

import javax.validation.ConstraintValidatorContext;

/**
 * 验证数据是否存在
 *
 * @author zj chen <britton@126.com>
 */
public interface GXValidateDBExistsService {
    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param value                      The value to check for
     * @param tableName                  database table name
     * @param fieldName                  The name of the field for which to check if the value exists
     * @param constraintValidatorContext validate constraint Object
     * @param param                      extract param
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean validateExists(Object value, String tableName, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException;
}