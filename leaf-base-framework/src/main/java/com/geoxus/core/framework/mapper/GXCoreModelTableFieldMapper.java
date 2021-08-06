package com.geoxus.core.framework.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.builder.GXCoreModelTableFieldBuilder;
import com.geoxus.core.framework.entity.GXCoreModelTableFieldEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GXCoreModelTableFieldMapper extends GXBaseMapper<GXCoreModelTableFieldEntity> {
    @SelectProvider(type = GXCoreModelTableFieldBuilder.class, method = "getModelAttributesByModelId")
    List<Dict> getModelAttributesByModelId(Dict condition);
}
