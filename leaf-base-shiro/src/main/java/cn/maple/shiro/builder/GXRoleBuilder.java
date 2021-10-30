package cn.maple.shiro.builder;

import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.shiro.constant.GXRoleConstant;

public class GXRoleBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXRoleConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
