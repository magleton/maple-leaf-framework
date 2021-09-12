package com.geoxus.commons.mappers;

import cn.hutool.core.lang.Dict;
import com.geoxus.commons.builder.GXRegionBuilder;
import com.geoxus.commons.entities.GXRegionEntity;
import com.geoxus.core.common.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface GXRegionMapper extends GXBaseMapper<GXRegionEntity> {
    @SelectProvider(type = GXRegionBuilder.class, method = "areaInfo")
    Dict areaInfo(Dict param);
}