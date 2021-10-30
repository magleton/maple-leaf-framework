package cn.maple.shiro.builder;

import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.shiro.constant.GXAdminConstant;

public class GXAdminBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXAdminConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
