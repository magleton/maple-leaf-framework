package com.geoxus.core.framework.annotation;

import java.lang.annotation.*;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:20
 * <p>
 * 测量一个方法运行的时间
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXStopWatch {
}
