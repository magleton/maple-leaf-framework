package cn.maple.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:20
 * <p>
 * 触发验证一个对象的动作
 */
@Target({ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXValidated {
    @GXFieldComment(zhDesc = "验证的组")
    Class<?>[] groups() default {};
}
