package cn.maple.core.framework.dao;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.GXBaseData;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.GXUnionTypeEnums;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.res.GXPaginationResDto;

import java.io.Serializable;
import java.util.List;

public interface GXBaseDao<T extends GXBaseData, ID extends Serializable> {
    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    ID updateOrCreate(T entity, List<GXCondition<?>> condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 影响的行数
     */
    Integer updateFieldByCondition(String tableName, List<GXUpdateField<?>> data, List<GXCondition<?>> condition);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(String tableName, List<GXCondition<?>> condition);

    /**
     * 通过条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 列表
     */
    Dict findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 通过条件获取数据
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return 列表
     */
    Dict findOneByCondition(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums);

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    List<Dict> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 通过条件获取数据列表
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return 列表
     */
    List<Dict> findByCondition(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName       表名
     * @param updateFieldList 软删除时需要同时更新的字段
     * @param condition       删除条件
     * @param extraData       额外数据
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, List<GXUpdateField<?>> updateFieldList, List<GXCondition<?>> condition, Dict extraData);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @param extraData 额外数据
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition, Dict extraData);

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 分页
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return GXPaginationResDto
     */
    GXPaginationResDto<Dict> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 分页
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return GXPaginationResDto
     */
    GXPaginationResDto<Dict> paginate(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums);

    /**
     * 获取表名字
     *
     * @return 数据库表的名字
     */
    String getTableName();
}
