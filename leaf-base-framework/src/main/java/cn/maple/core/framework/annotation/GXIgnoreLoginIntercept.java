package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

/**
 * 忽略需要登录认证的URL
 *
 * @author britton gapleaf@63.com
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXIgnoreLoginIntercept {
}
