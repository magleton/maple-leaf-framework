package com.geoxus.core.common.annotation;

import com.geoxus.common.annotation.GXFieldCommentAnnotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GxFeignHeaderAnnotation {
    @GXFieldCommentAnnotation("需要透传到被调用方的header参数名字")
    String[] names() default {};
}
