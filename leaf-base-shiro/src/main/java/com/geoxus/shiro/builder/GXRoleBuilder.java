package com.geoxus.shiro.builder;

import com.geoxus.core.builder.GXBaseBuilder;
import com.geoxus.shiro.constant.GXRoleConstant;

public class GXRoleBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXRoleConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
