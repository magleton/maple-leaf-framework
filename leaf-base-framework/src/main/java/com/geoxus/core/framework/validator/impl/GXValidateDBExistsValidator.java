package com.geoxus.core.framework.validator.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.core.framework.annotation.GXValidateDBExists;
import com.geoxus.core.framework.validator.GXValidateDBExistsService;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 验证数据是否存在的验证器
 *
 * @author zj chen <britton@126.com>
 */
@Slf4j
public class GXValidateDBExistsValidator implements ConstraintValidator<GXValidateDBExists, Object> {
    private GXValidateDBExistsService service;

    private String fieldName;

    private Class<?>[] groups;

    private String tableName;

    private String condition;

    private String spEL;

    @Override
    public void initialize(GXValidateDBExists annotation) {
        Class<? extends GXValidateDBExistsService> clazz = annotation.service();
        fieldName = annotation.fieldName();
        groups = annotation.groups();
        service = GXSpringContextUtil.getBean(clazz);
        tableName = annotation.tableName();
        condition = annotation.condition();
        spEL = annotation.spEL();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(o)) {
            throw new GXBusinessException(CharSequenceUtil.format("验证出错 , <{}>字段的值为<{}>", fieldName, o));
        }
        if (null == service) {
            throw new GXBusinessException(CharSequenceUtil.format("字段<{}>的值<{}>需要指定相应的Service进行验证...", fieldName, o));
        }
        Dict param = Dict.create()
                .set("tableName", tableName)
                .set("condition", condition)
                .set("spEL", spEL);
        return service.validateExists(o, fieldName, constraintValidatorContext, param);
    }
}