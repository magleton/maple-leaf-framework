package com.geoxus.shiro.builder;

import com.geoxus.core.datasource.builder.GXBaseBuilder;
import com.geoxus.shiro.constant.GXMenuPermissionsConstant;

public class GXMenuPermissionsBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXMenuPermissionsConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
