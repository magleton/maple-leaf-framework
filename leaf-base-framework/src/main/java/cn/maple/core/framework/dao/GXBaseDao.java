package cn.maple.core.framework.dao;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.GXBaseData;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.List;

public interface GXBaseDao<T extends GXBaseData, R extends GXBaseResDto, ID extends Serializable> {
    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    ID updateOrCreate(T entity, Table<String, String, Object> condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 影响的行数
     */
    Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition);

    /**
     * 通过条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 列表
     */
    R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 分页
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return GXPagination
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 获取表名字
     *
     * @return 数据库表的名字
     */
    String getTableName();
}
