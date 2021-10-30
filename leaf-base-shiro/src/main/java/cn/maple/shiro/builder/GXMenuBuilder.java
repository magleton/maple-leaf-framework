package cn.maple.shiro.builder;

import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.shiro.constant.GXMenuConstant;

public class GXMenuBuilder implements GXBaseBuilder {
    @Override
    public String getModelIdentificationValue() {
        return GXMenuConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
