package com.geoxus.core.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geoxus.core.builder.GXBaseBuilder;
import com.geoxus.core.handler.GXJsonToMapTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface GXBaseMapper<T> extends BaseMapper<T> {
    @UpdateProvider(type = GXBaseBuilder.class, method = "updateFieldByCondition")
    boolean updateFieldByCondition(String tableName, Dict data, Dict whereData);

    @UpdateProvider(type = GXBaseBuilder.class, method = "updateStatusByCondition")
    boolean updateStatusByCondition(String tableName, int status, Dict condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "checkRecordIsExists")
    Integer checkRecordIsExists(String tableName, Dict condition);

    @InsertProvider(type = GXBaseBuilder.class, method = "batchInsertBySql")
    Integer batchInsertBySql(String tableName, Set<String> fieldSet, List<Dict> dataList);

    @SelectProvider(type = GXBaseBuilder.class, method = "checkRecordIsUnique")
    Integer checkRecordIsUnique(String tableName, Dict condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "getFieldValueBySql")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class)
    })
    Dict getFieldValueBySql(String tableName, Set<String> fieldSet, Dict condition, boolean remove);
}
