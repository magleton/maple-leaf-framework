package com.geoxus.core.framework.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.builder.GXCoreAttributesBuilder;
import com.geoxus.core.framework.entity.GXCoreAttributesEntity;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GXCoreAttributesMapper extends GXBaseMapper<GXCoreAttributesEntity> {
    @Override
    @SelectProvider(type = GXCoreAttributesBuilder.class, method = "listOrSearch")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);

    @SelectProvider(type = GXCoreAttributesBuilder.class, method = "detail")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    Dict detail(Dict param);
}
