package cn.maple.core.datasource.mapper;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yulichang.base.MPJBaseMapper;
import com.google.common.collect.Table;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface GXBaseMapper<T extends GXMyBatisModel, R extends GXBaseResDto> extends MPJBaseMapper<T> {
    @UpdateProvider(type = GXBaseBuilder.class, method = "updateFieldByCondition")
    Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "checkRecordIsExists")
    Integer checkRecordIsExists(GXBaseQueryParamInnerDto queryParamInnerDto);

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
