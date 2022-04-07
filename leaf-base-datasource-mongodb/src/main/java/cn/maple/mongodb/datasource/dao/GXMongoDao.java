package cn.maple.mongodb.datasource.dao;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dao.GXBaseDao;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.mongodb.datasource.model.GXMongoModel;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.List;

public class GXMongoDao<T extends GXMongoModel, R extends GXBaseResDto, ID extends Serializable> implements GXBaseDao<T, R, ID> {
    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 影响的行数
     */
    @Override
    public Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        return null;
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        return false;
    }

    /**
     * 保存数据
     *
     * @param entity 需要保存的数据
     * @return ID
     */
    @Override
    public ID create(T entity) {
        return null;
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名字
     * @param dataList  数据集合
     * @return int
     */
    @Override
    public Integer saveBatch(String tableName, List<Dict> dataList) {
        return null;
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        return null;
    }

    /**
     * 通过条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 列表
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return null;
    }

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return null;
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(String tableName, Table<String, String, Object> condition) {
        return null;
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteCondition(String tableName, Table<String, String, Object> condition) {
        return null;
    }

    /**
     * 分页
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 分页数据
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return null;
    }

    /**
     * 分页  返回实体对象
     *
     * @param dbQueryParamInnerDto 查询条件
     * @param mapperMethodName     Mapper方法
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto, String mapperMethodName) {
        return null;
    }

    /**
     * 获取表名字
     *
     * @param clazz 实体的类型
     * @return 数据库表的名字
     */
    @Override
    public String getTableName(Class<T> clazz) {
        return null;
    }
}
