package com.geoxus.common.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.common.builder.GXCoreConfigBuilder;
import com.geoxus.common.entity.GXCoreConfigEntity;
import com.geoxus.core.datasource.handler.GXJsonToMapTypeHandler;
import com.geoxus.core.datasource.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GXCoreConfigMapper extends GXBaseMapper<GXCoreConfigEntity> {
    @SelectProvider(type = GXCoreConfigBuilder.class, method = "listOrSearch")
    <R> List<R> listOrSearchPage(IPage<R> page, Dict param);

    @SelectProvider(type = GXCoreConfigBuilder.class, method = "detail")
    @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    Dict detail(Dict param);
}
