package com.geoxus.feature.mappers;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.mapper.GXBaseMapper;
import com.geoxus.feature.builder.GXRegionBuilder;
import com.geoxus.feature.entities.GXRegionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface GXRegionMapper extends GXBaseMapper<GXRegionEntity> {
    @SelectProvider(type = GXRegionBuilder.class, method = "areaInfo")
    Dict areaInfo(Dict param);
}