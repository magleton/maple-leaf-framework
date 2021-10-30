package cn.maple.shiro.builder;

import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.shiro.constant.GXPermissionsConstant;

public class GXPermissionsBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXPermissionsConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
