package cn.maple.core.datasource.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.annotation.GXValidateDBUnique;
import cn.maple.core.datasource.service.GXValidateDBUniqueService;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 验证数据是否唯一验证器
 *
 * @author zj chen <britton@126.com>
 */
@Slf4j
@Service
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
        service = GXSpringContextUtils.getBean(clazz);
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
                .set("condition", condition)
                .set("spEL", spEL);
        return !service.validateUnique(o, tableName, fieldName, constraintValidatorContext, param);
    }
}