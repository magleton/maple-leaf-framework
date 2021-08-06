package com.geoxus.core.common.annotation;

import com.geoxus.core.common.validator.GXValidateDBUnique;
import com.geoxus.core.common.validator.impl.GXValidateDBUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author britton chen
 * @email britton@126.com
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = GXValidateDBUniqueValidator.class)
@Documented
public @interface GXValidateDBUniqueAnnotation {
    /**
     * 错误消息
     *
     * @return
     */
    String message() default "该数据已经存在";

    /**
     * 分组
     *
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * 数据
     *
     * @return
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 提供服务的类
     *
     * @return
     */
    Class<? extends GXValidateDBUnique> service();

    /**
     * 验证的字段名字
     *
     * @return
     */
    String fieldName() default "model_id";

    /**
     * 表名
     *
     * @return
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