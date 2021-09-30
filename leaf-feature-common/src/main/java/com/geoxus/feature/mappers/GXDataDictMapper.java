package com.geoxus.feature.mappers;

import cn.hutool.core.lang.Dict;
import com.geoxus.feature.builder.GXDataDictBuilder;
import com.geoxus.feature.entities.GXDataDictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GXDataDictMapper extends GXFrameworkBaseMapper<GXDataDictEntity> {
    @SelectProvider(type = GXDataDictBuilder.class, method = "listOrSearchPage")
    List<Dict> listOrSearchPage(Dict condition);
}