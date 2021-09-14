package com.geoxus.commons.annotation;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 限制功能在某段时间内只能被请求的次数
 * 比如: 发送短信的方法
 * 在一个小时内同一个IP地址只能发送5次......
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXFrequencyLimitAnnotation {
    @GXFieldCommentAnnotation(zhDesc = "值")
    @AliasFor("count")
    int value() default 10;

    @GXFieldCommentAnnotation(zhDesc = "限制的默认次数")
    @AliasFor("value")
    int count() default 10;

    @GXFieldCommentAnnotation(zhDesc = "限制的key")
    String key() default "";

    @GXFieldCommentAnnotation(zhDesc = "过期时间, 单位: 秒")
    int expire() default 600;

    @GXFieldCommentAnnotation(zhDesc = "场景值 使用IP限制还是使用其他的限制")
    String scene() default "ip";
}
