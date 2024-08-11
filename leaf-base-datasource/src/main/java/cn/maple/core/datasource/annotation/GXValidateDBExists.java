package cn.maple.core.datasource.annotation;

import cn.maple.core.datasource.service.GXValidateDBExistsService;
import cn.maple.core.datasource.service.impl.GXValidateDBExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

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
public @interface GXValidateDBExists {
    /**
     * 错误消息
     *
     * @return String
     */
    String message() default "{fieldName}对应的数据已经存在或是参数已经存在存在";

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
    Class<? extends GXValidateDBExistsService> service();

    /**
     * 目标字段名字
     *
     * @return String
     */
    String fieldName();

    /**
     * 表名
     *
     * @return String
     */
    String tableName();

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

    /**
     * 需要依赖的字段名字
     *
     * @return String[]
     */
    String[] dependOnFields() default {};
}