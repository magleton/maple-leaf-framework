package cn.maple.core.datasource.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.annotation.GXValidateDBExists;
import cn.maple.core.datasource.service.GXValidateDBExistsService;
import cn.maple.core.framework.dto.inner.GXValidateExistsDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 验证数据是否存在的验证器
 *
 * @author zj chen <britton@126.com>
 */
@Slf4j
public class GXValidateDBExistsValidator implements ConstraintValidator<GXValidateDBExists, Object> {
    /**
     * 执行验证的服务
     */
    private GXValidateDBExistsService service;

    /**
     * 需要验证的字段名字
     */
    private String fieldName;

    /**
     * 验证分组
     */
    private Class<?>[] groups;

    /**
     * 表名字
     */
    private String tableName;

    /**
     * 附加的查询条件
     * eg:
     * type='news',phone='13800138000',age=34
     */
    private String condition;

    /**
     * 附加条件 SpEL表达式
     * 用于计算结果是否满足预期
     */

    private String spEL;

    @Override
    public void initialize(GXValidateDBExists annotation) {
        Class<? extends GXValidateDBExistsService> clazz = annotation.service();
        fieldName = annotation.fieldName();
        groups = annotation.groups();
        service = GXSpringContextUtils.getBean(clazz);
        tableName = annotation.tableName();
        condition = annotation.condition();
        spEL = annotation.spEL();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(o)) {
            throw new GXBusinessException(CharSequenceUtil.format("验证出错 , <{}>字段的值为<{}>", fieldName, null));
        }

        if (null == service) {
            throw new GXBusinessException(CharSequenceUtil.format("字段<{}>的值<{}>需要指定相应的Service进行验证...", fieldName, o));
        }

        Dict conditionData = GXCommonUtils.convertStrToTarget("{" + condition + "}", Dict.class);

        if (Dict.class.isAssignableFrom(o.getClass())) {
            Dict data = Convert.convert(Dict.class, o);
            assert conditionData != null;
            conditionData.putAll(data);
        }

        GXValidateExistsDto validateExistsDto = GXValidateExistsDto.builder()
                .tableName(tableName)
                .fieldName(fieldName)
                .value(o)
                .condition(conditionData)
                .spEL(spEL)
                .groups(groups)
                .build();

        return service.validateExists(validateExistsDto, constraintValidatorContext);
    }
}