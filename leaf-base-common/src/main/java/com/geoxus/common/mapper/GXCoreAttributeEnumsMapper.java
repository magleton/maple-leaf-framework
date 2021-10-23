package com.geoxus.common.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.builder.GXCoreAttributeEnumsBuilder;
import com.geoxus.common.entity.GXCoreAttributesEnumsEntity;
import com.geoxus.core.datasource.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.IntegerTypeHandler;

import java.util.List;

@Mapper
public interface GXCoreAttributeEnumsMapper extends GXBaseMapper<GXCoreAttributesEnumsEntity> {
    @SelectProvider(type = GXCoreAttributeEnumsBuilder.class, method = "exists")
    @Results({
            @Result(column = "cnt", property = "count", typeHandler = IntegerTypeHandler.class)
    })
    int exists(Dict param);

/*    @Override
    @SelectProvider(type = GXCoreAttributeEnumsBuilder.class, method = "listOrSearch")
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);*/

    @SelectProvider(type = GXCoreAttributeEnumsBuilder.class, method = "listOrSearch")
    List<Dict> listOrSearch(Dict param);

    @SelectProvider(type = GXCoreAttributeEnumsBuilder.class, method = "getAttributeEnumsByAttributeIdAndCoreModelId")
    List<Dict> getAttributeEnumsByAttributeIdAndCoreModelId(Integer attributeId, Integer coreModelId);
}
