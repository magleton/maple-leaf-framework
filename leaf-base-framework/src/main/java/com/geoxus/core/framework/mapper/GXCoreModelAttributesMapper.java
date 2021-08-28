package com.geoxus.core.framework.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.builder.GXCoreModelAttributesBuilder;
import com.geoxus.core.framework.entity.GXCoreModelAttributesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GXCoreModelAttributesMapper extends GXBaseMapper<GXCoreModelAttributesEntity, Dict> {
    @SelectProvider(type = GXCoreModelAttributesBuilder.class, method = "getModelAttributesByCondition")
    List<Dict> getModelAttributesByCondition(Dict param);

    @SelectProvider(type = GXCoreModelAttributesBuilder.class, method = "checkCoreModelHasAttribute")
    Integer checkCoreModelHasAttribute(Dict param);
}
