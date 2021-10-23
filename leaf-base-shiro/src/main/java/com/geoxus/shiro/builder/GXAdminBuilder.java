package com.geoxus.shiro.builder;

import com.geoxus.core.datasource.builder.GXBaseBuilder;
import com.geoxus.shiro.constant.GXAdminConstant;

public class GXAdminBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXAdminConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
