package cn.maple.core.datasource.service;

import cn.maple.core.framework.dto.inner.GXValidateExistsDto;

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
     * @param validateExistsDto          dto param
     * @param constraintValidatorContext validate constraint Object
     * @return True if the value exists for the field; false otherwise
     */
    boolean validateExists(GXValidateExistsDto validateExistsDto, ConstraintValidatorContext constraintValidatorContext);
}