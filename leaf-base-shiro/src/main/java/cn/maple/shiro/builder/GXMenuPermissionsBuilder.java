package cn.maple.shiro.builder;

import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.shiro.constant.GXMenuPermissionsConstant;

public class GXMenuPermissionsBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXMenuPermissionsConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
