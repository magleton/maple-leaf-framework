package com.geoxus.core.common.validator.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.annotation.GXValidateDBUnique;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.core.common.validator.GXValidateDBUniqueService;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 验证数据是否唯一验证器
 *
 * @author zj chen <britton@126.com>
 */
@Slf4j
public class GXValidateDBUniqueValidator implements ConstraintValidator<GXValidateDBUnique, Object> {
    private GXValidateDBUniqueService service;

    private String fieldName;

    private String tableName;

    private String condition;

    private String spEL;

    @Override
    public void initialize(GXValidateDBUnique annotation) {
        Class<? extends GXValidateDBUniqueService> clazz = annotation.service();
        fieldName = annotation.fieldName();
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
        return !service.validateUnique(o, fieldName, constraintValidatorContext, param);
    }
}