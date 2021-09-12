package com.geoxus.core.framework.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.builder.GXCoreModelBuilder;
import com.geoxus.core.framework.entity.GXCoreModelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface GXCoreModelMapper extends GXBaseMapper<GXCoreModelEntity> {
    @SelectProvider(type = GXCoreModelBuilder.class, method = "getSearchCondition")
    @Results({
            @Result(column = "model_id", property = "model_id"),
            @Result(column = "search_condition", property = "search_condition")
    })
    Dict getSearchCondition(Dict param);
}
