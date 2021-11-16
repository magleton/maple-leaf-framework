package cn.maple.core.framework.annotation;

import cn.maple.core.framework.config.GXFrameworkConfig;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({GXFrameworkConfig.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface GXEnableLeafFramework {
}
