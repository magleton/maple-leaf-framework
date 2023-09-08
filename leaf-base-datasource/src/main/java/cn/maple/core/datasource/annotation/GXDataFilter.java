/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package cn.maple.core.datasource.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤注解
 *
 * @author 塵渊 britton@126.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXDataFilter {
    /**
     * 表的别名
     */
    String tableAlias() default "";

    /**
     * 用户ID
     */
    String[] userIdFieldNames() default {"user_id"};

    /**
     * 部门ID
     */
    String[] deptIdFieldNames() default {"dept_id"};
}