package cn.maple.core.framework.ddd.repository;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import com.google.common.collect.Table;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;

public interface GXBaseRepository<T, R extends GXBaseResDto, ID> {
    /**
     * 保存数据
     *
     * @param entity    需要保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    default ID create(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity 需要保存的数据
     * @return ID
     */
    @SuppressWarnings("all")
    ID create(T entity);

    /**
     * 保存数据
     *
     * @param entity    需要更新的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    default ID update(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    ID updateOrCreate(T entity, Table<String, String, Object> condition);

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @return 列表
     */
    <E> List<E> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz);

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询条件
     * @return 列表
     */
    List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto);

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @param columns   需要获取的列
     * @return 列表
     */
    List<R> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns);

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @return 列表
     */
    List<R> findByCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 根据条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return R 返回数据
     */
    R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return R 返回数据
     */
    R findOneByCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @param columns   需要查询的列
     * @return R 返回数据
     */
    R findOneByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns);

    /**
     * 根据条件获取分页数据
     *
     * @param dbQueryParamInnerDto 条件查询
     * @return 分页数据
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

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
    GXPaginationResDto<R> paginate(Integer page, Integer pageSize, String tableName, Table<String, String, Object> condition, Set<String> columns);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftWhere(String tableName, Table<String, String, Object> condition);

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteWhere(String tableName, Table<String, String, Object> condition);

    /**
     * 检测数据是否存在
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 存在 0 不存在
     */
    boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition);

    /**
     * 实现验证注解(返回true表示数据已经存在)
     *
     * @param value                      The value to check for
     * @param tableName                  database table name
     * @param fieldName                  The name of the field for which to check if the value exists
     * @param constraintValidatorContext The ValidatorContext
     * @param param                      param
     * @return boolean
     */
    boolean validateExists(Object value, String tableName, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param)
            throws UnsupportedOperationException;

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param dataList  数据集合
     * @return 影响行数
     */
    Integer batchInsert(String tableName, List<Dict> dataList);

    /**
     * 通过条件更新数据
     *
     * @param tableName 需要更新的表名
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 更新的条数
     */
    boolean updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition);


    /**
     * 获取 Primary Key
     *
     * @return String
     */
    default String getPrimaryKey() {
        return "id";
    }
}
