package com.geoxus.core.framework.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.builder.GXFrameWorkBuilder;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.core.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Set;

@Mapper
public interface GXFrameworkBaseMapper<T> extends GXBaseMapper<T> {
    @SelectProvider(type = GXFrameWorkBuilder.class, method = "getFieldValueBySql")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    Dict getFieldValueBySql(String tableName, Set<String> fieldSet, Dict condition, boolean remove);
}
