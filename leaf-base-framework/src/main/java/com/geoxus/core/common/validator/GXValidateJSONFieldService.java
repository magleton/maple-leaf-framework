package com.geoxus.core.common.validator;

import javax.validation.ConstraintValidatorContext;

/**
 * 验证模型的扩展数据
 */
public interface GXValidateJSONFieldService {
    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param o                    The value to check for
     * @param modelIdentification  The name of the model
     * @param tableField           Table Field
     * @param isFullMatchAttribute is full Match
     * @param context              context
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean validateJSONFieldData(Object o, String modelIdentification, String tableField, boolean isFullMatchAttribute, ConstraintValidatorContext context) throws UnsupportedOperationException;
}
