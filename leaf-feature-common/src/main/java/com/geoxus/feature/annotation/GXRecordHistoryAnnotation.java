package com.geoxus.feature.annotation;

import com.geoxus.core.framework.service.GXBaseService;
import com.geoxus.core.framework.service.impl.GXCoreModelServiceImpl;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXRecordHistoryAnnotation {
    String originTableName();

    String historyTableName();

    String[] conditionalParameterName() default {};

    Class<? extends GXBaseService<?, ?, ?>> service() default GXCoreModelServiceImpl.class;
}
