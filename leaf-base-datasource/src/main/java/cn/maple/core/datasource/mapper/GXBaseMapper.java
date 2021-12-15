package cn.maple.core.datasource.mapper;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.google.common.collect.Table;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GXBaseMapper<T extends GXMyBatisModel, R extends GXBaseResDto> extends BaseMapper<T> {
    @UpdateProvider(type = GXBaseBuilder.class, method = "updateFieldByCondition")
    Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "checkRecordIsExists")
    Integer checkRecordIsExists(GXBaseQueryParamInnerDto queryParamInnerDto);

    @InsertProvider(type = GXBaseBuilder.class, method = "batchInsert")
    Integer batchInsert(String tableName, List<Dict> dataList);

    @SelectProvider(type = GXBaseBuilder.class, method = "findOneByCondition")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = JacksonTypeHandler.class)
    })
    R findOneByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto);

    @SelectProvider(type = GXBaseBuilder.class, method = "findByCondition")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = JacksonTypeHandler.class)
    })
    List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto);

    @SelectProvider(type = GXBaseBuilder.class, method = "paginate")
    @Results(value = {
            @Result(column = "ext", property = "ext", typeHandler = JacksonTypeHandler.class)
    })
    List<R> paginate(IPage<R> page, GXBaseQueryParamInnerDto dbQueryInnerDto);

    @UpdateProvider(type = GXBaseBuilder.class, method = "deleteSoftWhere")
    Integer deleteSoftWhere(String tableName, Table<String, String, Object> condition);

    @DeleteProvider(type = GXBaseBuilder.class, method = "deleteWhere")
    Integer deleteWhere(String tableName, Table<String, String, Object> condition);
}
