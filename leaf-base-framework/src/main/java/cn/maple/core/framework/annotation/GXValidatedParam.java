package cn.maple.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXValidatedParam {
    @GXFieldComment(zhDesc = "验证的组")
    Class<?>[] groups() default {};
}
