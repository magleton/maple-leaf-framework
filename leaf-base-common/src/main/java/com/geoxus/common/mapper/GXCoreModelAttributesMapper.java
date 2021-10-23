package com.geoxus.common.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.builder.GXCoreModelAttributesBuilder;
import com.geoxus.common.entity.GXCoreModelAttributesEntity;
import com.geoxus.core.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GXCoreModelAttributesMapper extends GXBaseMapper<GXCoreModelAttributesEntity> {
    @SelectProvider(type = GXCoreModelAttributesBuilder.class, method = "getModelAttributesByCondition")
    List<Dict> getModelAttributesByCondition(Dict param);

    @SelectProvider(type = GXCoreModelAttributesBuilder.class, method = "checkCoreModelHasAttribute")
    Integer checkCoreModelHasAttribute(Dict param);
}
