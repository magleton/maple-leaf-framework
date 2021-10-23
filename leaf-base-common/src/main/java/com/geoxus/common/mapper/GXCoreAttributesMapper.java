package com.geoxus.common.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.common.builder.GXCoreAttributesBuilder;
import com.geoxus.common.entity.GXCoreAttributesEntity;
import com.geoxus.core.datasource.handler.GXJsonToMapTypeHandler;
import com.geoxus.core.datasource.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GXCoreAttributesMapper extends GXBaseMapper<GXCoreAttributesEntity> {
    @SelectProvider(type = GXCoreAttributesBuilder.class, method = "listOrSearch")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    <R> List<R> listOrSearchPage(IPage<R> page, Dict param);

    @SelectProvider(type = GXCoreAttributesBuilder.class, method = "detail")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    Dict detail(Dict param);
}
