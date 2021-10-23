package com.geoxus.shiro.builder;

import com.geoxus.core.datasource.builder.GXBaseBuilder;
import com.geoxus.shiro.constant.GXPermissionsConstant;

public class GXPermissionsBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXPermissionsConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
