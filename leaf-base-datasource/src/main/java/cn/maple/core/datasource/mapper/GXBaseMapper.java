package cn.maple.core.datasource.mapper;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.google.common.collect.Table;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface GXBaseMapper<T extends GXBaseEntity, R extends GXBaseResDto> extends BaseMapper<T> {
    @UpdateProvider(type = GXBaseBuilder.class, method = "updateFieldByCondition")
    boolean updateFieldByCondition(String tableName, Dict data, Dict whereData);

    @SelectProvider(type = GXBaseBuilder.class, method = "checkRecordIsExists")
    Integer checkRecordIsExists(String tableName, Dict condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "checkRecordIsUnique")
    Integer checkRecordIsUnique(String tableName, Dict condition);

    @InsertProvider(type = GXBaseBuilder.class, method = "batchInsert")
    Integer batchInsert(String tableName, Set<String> fieldSet, List<Dict> dataList);

    @SelectProvider(type = GXBaseBuilder.class, method = "getDataByCondition")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = JacksonTypeHandler.class)
    })
    R getDataByCondition(String tableName, Set<String> fieldSet, Table<String, String, Object> condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "getDataByCondition")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = JacksonTypeHandler.class)
    })
    List<R> getListByCondition(String tableName, Set<String> fieldSet, Table<String, String, Object> condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "getPageByCondition")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = JacksonTypeHandler.class)
    })
    List<R> getPageByCondition(IPage<R> page, String tableName, Set<String> fieldSet, Table<String, String, Object> condition);
}
