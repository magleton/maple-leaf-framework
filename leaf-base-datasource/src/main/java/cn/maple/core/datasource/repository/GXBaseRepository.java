package cn.maple.core.datasource.repository;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Set;

public interface GXBaseRepository<T extends GXBaseEntity, R extends GXBaseResDto> {
    /**
     * 保存数据
     *
     * @param entity    需要保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    default Integer create(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    default Integer update(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    default Integer updateOrCreate(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 根据条件获取所有数据
     *
     * @param columns   需要获取的列
     * @param condition 条件
     * @return 列表
     */
    default List<R> findByCondition(Set<String> columns, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return R 返回数据
     */
    default R findOneByCondition(Set<String> columns, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 根据条件获取分页数据
     *
     * @param page      当前页
     * @param pageSize  每页大小
     * @param condition 查询条件
     * @param columns   需要的数据列
     * @return 分页对象
     */
    default GXPaginationResDto<R> paginate(Integer page, Integer pageSize, Table<String, String, Object> condition, Set<String> columns) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 根据条件删除
     *
     * @param whereCondition 删除条件
     * @return 影响行数
     */
    default Integer deleteWhere(Table<String, String, Object> whereCondition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 检测数据是否存在
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 存在 0 不存在
     */
    default Integer checkRecordIsExists(String tableName, Dict condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 检测数据是否唯一
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 不唯一 0 唯一
     */
    default Integer checkRecordIsUnique(String tableName, Dict condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param fieldSet  需要插入的字段集合
     * @param dataList  数据集合
     * @return 影响行数
     */
    default Integer batchInsert(String tableName, Set<String> fieldSet, List<Dict> dataList) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 通过条件更新数据
     *
     * @param tableName 需要更新的表名
     * @param fieldSet  需要更新的字段集合
     * @param dataList  数据集合
     * @param condition 更新条件
     * @return 更新的条数
     */
    default Integer updateDataByCondition(String tableName, Set<String> fieldSet, List<Dict> dataList, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }
}
