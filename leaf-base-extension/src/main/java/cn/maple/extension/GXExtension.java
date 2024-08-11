package cn.maple.extension;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(GXExtensions.class)
public @interface GXExtension {
    /**
     * 业务ID
     */
    String bizId() default GXBizScenario.DEFAULT_BIZ_ID;

    /**
     * 用例
     */
    String useCase() default GXBizScenario.DEFAULT_USE_CASE;

    /**
     * 场景
     */
    String scenario() default GXBizScenario.DEFAULT_SCENARIO;
}
