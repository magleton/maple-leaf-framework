package cn.maple.core.datasource.service;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.dto.inner.GXDBQueryParamInnerDto;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.repository.GXBaseRepository;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.service.GXBusinessService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Table;

import java.util.List;

/**
 * 业务DB基础Service
 *
 * @param <P> 仓库对象类型
 * @param <M> Mapper类型
 * @param <D> DAO类型
 * @param <T> 实体类型
 * @param <R> 响应对象类型
 * @author britton chen <britton@126.com>
 */
public interface GXDBBaseService<P extends GXBaseRepository<M, T, D, R, ID>,
        M extends GXBaseMapper<T, R>,
        T extends GXBaseEntity,
        D extends GXBaseDao<M, T, R>,
        R extends GXBaseResDto,
        ID>
        extends GXBusinessService, GXValidateDBExistsService {
    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    boolean updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition);

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名
     * @param dataList  数据集合
     * @return int
     */
    Integer batchInsert(String tableName, List<Dict> dataList);

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    GXPaginationResDto<R> paginate(GXDBQueryParamInnerDto searchReqDto);

    /**
     * 通过条件查询列表信息
     *
     * @param searchReqDto 搜索条件
     * @return List
     */
    List<R> findByCondition(GXDBQueryParamInnerDto searchReqDto);

    /**
     * 通过条件获取一条数据
     *
     * @param searchReqDto 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(GXDBQueryParamInnerDto searchReqDto);

    /**
     * 创建数据
     *
     * @param entity 数据实体
     * @return ID
     */
    ID create(T entity);

    /**
     * 更新数据
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    ID update(T entity, Table<String, String, Object> condition);

    /**
     * 更新数据
     *
     * @param entity        数据实体
     * @param updateWrapper 更新条件
     * @return ID
     */
    ID update(T entity, UpdateWrapper<T> updateWrapper);

    /**
     * 创建或者更新
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    ID updateOrCreate(T entity, Table<String, String, Object> condition);

    /**
     * 创建或者更新
     *
     * @param entity        数据实体
     * @param updateWrapper 更新条件
     * @return ID
     */
    ID updateOrCreate(T entity, UpdateWrapper<T> updateWrapper);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKey();
}
