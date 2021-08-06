package com.geoxus.core.common.validator;

import cn.hutool.core.lang.Dict;

import javax.validation.ConstraintValidatorContext;

/**
 * 验证数据是否唯一
 *
 * @author zj chen <britton@126.com>
 */
public interface GXValidateDBUnique {
    /**
     * Checks whether or not a given value unique
     * for a given field
     *
     * @param value     The value to check for
     * @param fieldName The name of the field for which to check if the value exists
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean validateUnique(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException;
}