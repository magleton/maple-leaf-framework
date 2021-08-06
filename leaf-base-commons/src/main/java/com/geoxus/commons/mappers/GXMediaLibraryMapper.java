package com.geoxus.commons.mappers;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.commons.builder.GXCoreMediaLibraryBuilder;
import com.geoxus.commons.entities.GXMediaLibraryEntity;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToListTypeHandler;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GXMediaLibraryMapper extends GXBaseMapper<GXMediaLibraryEntity> {
    @Override
    @SelectProvider(type = GXCoreMediaLibraryBuilder.class, method = "listOrSearch")
    @ResultMap("mediaResult")
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);

    @SelectProvider(type = GXCoreMediaLibraryBuilder.class, method = "detail")
    @Results(id = "mediaResult", value = {
            @Result(column = "custom_properties", property = "customProperties", typeHandler = GXJsonToListTypeHandler.class),
            @Result(column = "responsive_images", property = "responsiveImages", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "manipulations", property = "manipulations", typeHandler = GXJsonToListTypeHandler.class)
    })
    Dict detail(Dict param);

    @SelectProvider(type = GXCoreMediaLibraryBuilder.class, method = "baseInfoDetail")
    Dict baseInfoDetail(Dict param);

    @DeleteProvider(type = GXCoreMediaLibraryBuilder.class, method = "deleteByCondition")
    boolean deleteByCondition(Dict param);

    @SelectProvider(type = GXCoreMediaLibraryBuilder.class, method = "getMediaByCondition")
    List<Dict> getMediaByCondition(Dict param);
}
