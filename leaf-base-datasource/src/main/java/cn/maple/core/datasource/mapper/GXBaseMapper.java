package cn.maple.core.datasource.mapper;

import cn.maple.core.datasource.builder.GXBaseBuilder;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface GXBaseMapper<T extends GXMyBatisModel, R extends GXBaseResDto> extends BaseMapper<T> {
    @UpdateProvider(type = GXBaseBuilder.class, method = "updateFieldByCondition")
    Integer updateFieldByCondition(String tableName, List<GXUpdateField<?>> fieldList, List<GXCondition<?>> condition);

    @SelectProvider(type = GXBaseBuilder.class, method = "checkRecordIsExists")
    Integer checkRecordIsExists(GXBaseQueryParamInnerDto queryParamInnerDto);

    @SelectProvider(type = GXBaseBuilder.class, method = "findOneByCondition")
    R findOneByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto);

    @SelectProvider(type = GXBaseBuilder.class, method = "findByCondition")
    List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto);

    @SelectProvider(type = GXBaseBuilder.class, method = "paginate")
    List<R> paginate(IPage<R> page, GXBaseQueryParamInnerDto dbQueryInnerDto);

    @UpdateProvider(type = GXBaseBuilder.class, method = "deleteSoftCondition")
    Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition);

    @DeleteProvider(type = GXBaseBuilder.class, method = "deleteCondition")
    Integer deleteCondition(String tableName, List<GXCondition<?>> condition);
}
