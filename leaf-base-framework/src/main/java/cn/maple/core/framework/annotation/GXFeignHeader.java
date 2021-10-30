package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXFeignHeader {
    @GXFieldComment("需要透传到被调用方的header参数名字")
    String[] names() default {};
}
