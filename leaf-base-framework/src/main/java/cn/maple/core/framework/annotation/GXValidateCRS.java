package cn.maple.core.framework.annotation;

import cn.maple.core.framework.service.GXValidateCallRemoteService;
import cn.maple.core.framework.validator.GXValidateCallRemoteServiceValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 调用指定服务进行数据验证
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = GXValidateCallRemoteServiceValidator.class)
@Documented
public @interface GXValidateCRS {
    /**
     * 目标服务
     *
     * @return Class
     */
    Class<? extends GXValidateCallRemoteService> service();
}
