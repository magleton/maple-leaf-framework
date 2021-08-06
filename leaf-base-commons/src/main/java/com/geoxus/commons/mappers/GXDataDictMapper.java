package com.geoxus.commons.mappers;

import cn.hutool.core.lang.Dict;
import com.geoxus.commons.builder.GXDataDictBuilder;
import com.geoxus.commons.entities.GXDataDictEntity;
import com.geoxus.core.common.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GXDataDictMapper extends GXBaseMapper<GXDataDictEntity> {
    @SelectProvider(type = GXDataDictBuilder.class, method = "listOrSearchPage")
    List<Dict> listOrSearchPage(Dict condition);
}