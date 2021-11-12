package cn.maple.core.datasource.repository;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public abstract class GXBaseRepository<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, D extends GXBaseDao<M, T, R>, R extends GXBaseResDto> {
    /**
     * 基础DAO
     */
    @Autowired
    protected D baseDao;

    /**
     * 保存数据
     *
     * @param entity    需要保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    public Integer create(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    public Integer update(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    public Integer updateOrCreate(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 根据条件获取所有数据
     *
     * @param columns   需要获取的列
     * @param condition 条件
     * @return 列表
     */
    public List<R> findByCondition(Set<String> columns, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return R 返回数据
     */
    public R findOneByCondition(Set<String> columns, Table<String, String, Object> condition) {
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
    public GXPaginationResDto<R> paginate(Integer page, Integer pageSize, Table<String, String, Object> condition, Set<String> columns) {
        Page<R> iPage = new Page<>(page, pageSize);
        IPage<R> paginate = baseDao.paginate(iPage, condition, "paginate");
        return GXDBCommonUtils.convertPageToPaginationResDto(paginate);
    }

    /**
     * 根据条件删除
     *
     * @param whereCondition 删除条件
     * @return 影响行数
     */
    public Integer deleteWhere(Table<String, String, Object> whereCondition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 检测数据是否存在
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 存在 0 不存在
     */
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        return baseDao.checkRecordIsExists(tableName, condition);
    }

    /**
     * 检测数据是否唯一
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 不唯一 0 唯一
     */
    public boolean checkRecordIsUnique(String tableName, Table<String, String, Object> condition) {
        return baseDao.checkRecordIsUnique(tableName, condition);
    }

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param fieldSet  需要插入的字段集合
     * @param dataList  数据集合
     * @return 影响行数
     */
    public Integer batchInsert(String tableName, Set<String> fieldSet, List<Dict> dataList) {
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
    public Integer updateDataByCondition(String tableName, Set<String> fieldSet, List<Dict> dataList, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }
}
