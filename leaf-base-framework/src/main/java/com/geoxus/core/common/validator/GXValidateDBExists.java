package com.geoxus.core.common.validator;

import cn.hutool.core.lang.Dict;

import javax.validation.ConstraintValidatorContext;

/**
 * 验证数据是否存在
 *
 * @author zj chen <britton@126.com>
 */
public interface GXValidateDBExists {
    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param value The value to check for
     * @param fieldName The name of the field for which to check if the value exists
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean validateExists(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException;
}