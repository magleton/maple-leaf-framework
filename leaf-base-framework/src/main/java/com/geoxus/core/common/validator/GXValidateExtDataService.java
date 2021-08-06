package com.geoxus.core.common.validator;

import javax.validation.ConstraintValidatorContext;

/**
 * 验证模型的扩展数据
 */
public interface GXValidateExtDataService {
    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param o                    The value to check for
     * @param modelIdentification  The name of the model
     * @param tableFiled           Table Field
     * @param isFullMatchAttribute is full Match
     * @param context              context
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    boolean validateExtData(Object o, String modelIdentification, String tableFiled, boolean isFullMatchAttribute, ConstraintValidatorContext context) throws UnsupportedOperationException;
}
