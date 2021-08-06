package com.geoxus.core.common.annotation;

import com.geoxus.core.common.validator.GXValidateDBExists;
import com.geoxus.core.common.validator.impl.GXValidateDBExistsValidator;
import com.geoxus.core.framework.service.GXCoreModelService;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author britton chen <britton@126.com>
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = GXValidateDBExistsValidator.class)
@Documented
public @interface GXValidateDBExistsAnnotation {
    /**
     * 错误消息
     *
     * @return String
     */
    String message() default "{fieldName}对应的数据不存在或是参数不存在";

    /**
     * 分组验证
     *
     * @return Class
     */
    Class<?>[] groups() default {};

    /**
     * 数据
     *
     * @return Class
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 目标服务
     *
     * @return Class
     */
    Class<? extends GXValidateDBExists> service() default GXCoreModelService.class;

    /**
     * 目标字段名字
     *
     * @return String
     */
    String fieldName() default "model_id";

    /**
     * 表名
     *
     * @return String
     */
    String tableName() default "";

    /**
     * 附加的查询条件
     * eg:
     * type=news,phone=13800138000
     *
     * @return String
     */
    String condition() default "";

    /**
     * 附加条件 SpEL表达式
     * 用于计算结果是否满足预期
     *
     * @return String
     */
    String spEL() default "";
}