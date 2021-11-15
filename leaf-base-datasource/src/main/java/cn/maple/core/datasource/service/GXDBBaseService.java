package cn.maple.core.datasource.service;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.repository.GXBaseRepository;
import cn.maple.core.framework.dto.inner.req.GXBaseQueryParamReqDto;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.service.GXBusinessService;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Set;

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
public interface GXDBBaseService<P extends GXBaseRepository<M, T, D, R>,
        M extends GXBaseMapper<T, R>,
        D extends GXBaseDao<M, T, R>,
        T extends GXBaseEntity,
        R extends GXBaseResDto>
        extends GXBusinessService, GXValidateDBExistsService, GXValidateDBUniqueService {
    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition);

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsUnique(String tableName, Table<String, String, Object> condition);

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
     * @param fieldSet  字段集合
     * @param dataList  数据集合
     * @return int
     */
    Integer batchInsert(String tableName, Set<String> fieldSet, List<Dict> dataList);

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamReqDto searchReqDto);

    /**
     * 获取实体的表明
     *
     * @param clazz Class对象
     * @return String
     */
    String getTableName(Class<T> clazz);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKey();
}
