package com.geoxus.shiro.builder;

import com.geoxus.core.datasource.builder.GXBaseBuilder;
import com.geoxus.shiro.constant.GXMenuConstant;

public class GXMenuBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXMenuConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
