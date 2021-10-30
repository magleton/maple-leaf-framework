package cn.maple.core.framework.annotation;

import cn.maple.core.framework.config.GXFrameworkConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({GXFrameworkConfig.class})
public @interface GXEnableLeafFramework {
}
