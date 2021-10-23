package com.geoxus.common.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.builder.GXCoreModelBuilder;
import com.geoxus.common.entity.GXCoreModelEntity;
import com.geoxus.core.datasource.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface GXCoreModelMapper extends GXBaseMapper<GXCoreModelEntity> {
    @SelectProvider(type = GXCoreModelBuilder.class, method = "getSearchCondition")
    @Result(column = "model_id", property = "model_id")
    @Result(column = "search_condition", property = "search_condition")
    Dict getSearchCondition(Dict param);
}
