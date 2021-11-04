package cn.maple.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解需要配合GXValidated注解一起使用
 */
@Target({ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXValidParam {
    @GXFieldComment(zhDesc = "验证的组")
    Class<?>[] groups() default {};
}
