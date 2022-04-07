package cn.maple.core.datasource.mapper;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Table;
import org.apache.ibatis.annotations.*;
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper;

import java.util.List;

@Mapper
public interface GXBaseMapper<T extends GXMyBatisModel, R extends GXBaseResDto> extends BaseMapper<T>, CommonSelectMapper {
    @UpdateProvider(type = GXBaseBuilder.class, method = "updateFieldByCondition")
    Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "checkRecordIsExists")
    Integer checkRecordIsExists(GXBaseQueryParamInnerDto queryParamInnerDto);

    @InsertProvider(type = GXBaseBuilder.class, method = "saveBatch")
    Integer saveBatch(String tableName, List<Dict> dataList);

    @InsertProvider(type = GXBaseBuilder.class, method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "param2.id", keyColumn = "id")
    Integer save(String tableName, Dict data);

    @SelectProvider(type = GXBaseBuilder.class, method = "findOneByCondition")
    R findOneByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto);

    @SelectProvider(type = GXBaseBuilder.class, method = "findByCondition")
    List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto);

    @SelectProvider(type = GXBaseBuilder.class, method = "paginate")
    List<R> paginate(IPage<R> page, GXBaseQueryParamInnerDto dbQueryInnerDto);

    @UpdateProvider(type = GXBaseBuilder.class, method = "deleteSoftCondition")
    Integer deleteSoftCondition(String tableName, Table<String, String, Object> condition);

    @DeleteProvider(type = GXBaseBuilder.class, method = "deleteCondition")
    Integer deleteCondition(String tableName, Table<String, String, Object> condition);
}
