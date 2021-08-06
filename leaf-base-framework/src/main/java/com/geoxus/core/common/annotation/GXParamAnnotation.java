package com.geoxus.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXParamAnnotation {
    @GXFieldCommentAnnotation(zhDesc = "参数名字")
    String[] paramNames();

    @GXFieldCommentAnnotation(zhDesc = "是否必须")
    boolean require() default true;
}
