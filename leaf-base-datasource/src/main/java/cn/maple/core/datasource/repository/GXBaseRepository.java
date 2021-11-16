package cn.maple.core.datasource.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class GXBaseRepository<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, D extends GXBaseDao<M, T, R>, R extends GXBaseResDto, ID> {
    /**
     * 基础DAO
     */
    @SuppressWarnings("all")
    @Autowired
    protected D baseDao;

    /**
     * 保存数据
     *
     * @param entity    需要保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    public ID create(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    public ID update(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @return 列表
     */
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition) {
        return baseDao.findByCondition(tableName, condition, CollUtil.newHashSet("*"));
    }

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @param columns   需要获取的列
     * @return 列表
     */
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns) {
        return baseDao.findByCondition(tableName, condition, columns);
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return R 返回数据
     */
    public R findOneByCondition(String tableName, Table<String, String, Object> condition) {
        return baseDao.findOneByCondition(tableName, condition, CollUtil.newHashSet("*"));
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @param columns   需要查询的列
     * @return R 返回数据
     */
    public R findOneByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns) {
        return baseDao.findOneByCondition(tableName, condition, columns);
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
    public GXPaginationResDto<R> paginate(Integer page, Integer pageSize, Table<String, String, Object> condition, String mapperMethodName, Set<String> columns) {
        if (Objects.isNull(mapperMethodName)) {
            mapperMethodName = "paginate";
        }
        if (Objects.isNull(columns)) {
            columns = CollUtil.newHashSet("*");
        }
        Page<R> iPage = new Page<>(page, pageSize);
        IPage<R> paginate = baseDao.paginate(iPage, condition, mapperMethodName, columns);
        return GXDBCommonUtils.convertPageToPaginationResDto(paginate);
    }

    /**
     * 根据条件获取分页数据
     *
     * @param page      当前页
     * @param pageSize  每页大小
     * @param tableName 表名字
     * @param condition 查询条件
     * @param columns   需要的数据列
     * @return 分页对象
     */
    public GXPaginationResDto<R> paginate(Integer page, Integer pageSize, String tableName, Table<String, String, Object> condition, Set<String> columns) {
        if (Objects.isNull(columns)) {
            columns = CollUtil.newHashSet("*");
        }
        Page<R> iPage = new Page<>(page, pageSize);
        List<R> paginate = baseDao.paginate(iPage, tableName, condition, columns);
        return GXDBCommonUtils.convertPageToPaginationResDto(iPage, paginate);
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    public Integer deleteSoftWhere(String tableName, Table<String, String, Object> condition) {
        return baseDao.deleteSoftWhere(tableName, condition);
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    public Integer deleteWhere(String tableName, Table<String, String, Object> condition) {
        return baseDao.deleteWhere(tableName, condition);
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
     * 实现验证注解(返回true表示数据已经存在)
     *
     * @param value                      The value to check for
     * @param fieldName                  The name of the field for which to check if the value exists
     * @param constraintValidatorContext The ValidatorContext
     * @param param                      param
     * @return boolean
     */
    public boolean validateExists(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        String tableName = GXCommonUtils.getValueFromDict(param, "tableName", String.class);
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        Table<String, String, Object> condition = HashBasedTable.create();
        condition.put(fieldName, "=", value);
        return checkRecordIsExists(tableName, condition);
    }

    /**
     * 验证数据的唯一性 (返回true表示数据已经存在)
     *
     * @param value                      值
     * @param fieldName                  字段名字
     * @param constraintValidatorContext 验证上下文对象
     * @param param                      参数
     * @return boolean
     */
    public boolean validateUnique(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) {
        String tableName = GXCommonUtils.getValueFromDict(param, "tableName", String.class);
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        Table<String, String, Object> condition = HashBasedTable.create();
        condition.put(fieldName, "=", value);
        return checkRecordIsUnique(tableName, condition);
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

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    public String getPrimaryKey() {
        return "id";
    }
}
