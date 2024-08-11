package cn.maple.extension;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * because {@link GXExtension} only supports single coordinates,
 * this annotation is a supplement to {@link GXExtension} and supports multiple coordinates
 *
 * @author wangguoqiang wrote on 2022/10/10 12:19
 * @version 1.0
 * @see GXExtension
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface GXExtensions {

    String[] bizId() default GXBizScenario.DEFAULT_BIZ_ID;

    String[] useCase() default GXBizScenario.DEFAULT_USE_CASE;

    String[] scenario() default GXBizScenario.DEFAULT_SCENARIO;

    GXExtension[] value() default {};
}