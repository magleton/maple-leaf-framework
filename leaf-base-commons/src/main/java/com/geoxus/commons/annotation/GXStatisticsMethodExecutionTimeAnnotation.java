package com.geoxus.commons.annotation;

import java.lang.annotation.*;

/**
 * 方法的执行时间
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXStatisticsMethodExecutionTimeAnnotation {
}
