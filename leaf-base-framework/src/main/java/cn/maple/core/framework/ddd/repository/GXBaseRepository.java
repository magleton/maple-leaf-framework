package cn.maple.core.framework.ddd.repository;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.GXValidateExistsDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.res.GXPaginationResDto;

import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface GXBaseRepository<T, ID extends Serializable> {
    /**
     * 保存或者更新数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    ID updateOrCreate(T entity, List<GXCondition<?>> condition);

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    ID updateOrCreate(T entity);

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    List<Dict> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @return 列表
     */
    List<Dict> findByCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @param columns   需要查询的列名字
     * @return 列表
     */
    List<Dict> findByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns);

    /**
     * 根据条件获取所有数据
     *
     * @param condition 条件
     * @return 列表
     */
    default List<Dict> findByCondition(List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
        return findByCondition(getTableName(), condition);
    }

    /**
     * 获取所有数据
     *
     * @return 列表
     */
    default List<Dict> findByCondition() {
        return findByCondition(getTableName(), Collections.emptyList());
    }

    /**
     * 根据条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return R 返回数据
     */
    Dict findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return R 返回数据
     */
    Dict findOneByCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return R 返回数据
     */
    default Dict findOneByCondition(List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
        return findOneByCondition(getTableName(), condition);
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @param columns   需要查询的列
     * @return R 返回数据
     */
    Dict findOneByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns);

    /**
     * 通过ID获取一条记录
     *
     * @param tableName 表名字
     * @param id        ID值
     * @param columns   需要返回的列
     * @return 返回数据
     */
    Dict findOneById(String tableName, ID id, Set<String> columns);

    /**
     * 通过ID获取一条记录
     *
     * @param tableName 表名字
     * @param id        ID值
     * @return 返回数据
     */
    Dict findOneById(String tableName, ID id);

    /**
     * 根据条件获取分页数据
     *
     * @param dbQueryParamInnerDto 条件查询
     * @return 分页数据
     */
    GXPaginationResDto<Dict> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件获取分页数据
     *
     * @param tableName 表名字
     * @param page      当前页
     * @param pageSize  每页大小
     * @param condition 查询条件
     * @param columns   需要的数据列
     * @return 分页对象
     */
    GXPaginationResDto<Dict> paginate(String tableName, Integer page, Integer pageSize, List<GXCondition<?>> condition, Set<String> columns);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 检测数据是否存在
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 存在 0 不存在
     */
    boolean checkRecordIsExists(String tableName, List<GXCondition<?>> condition);

    /**
     * 实现验证注解(返回true表示数据已经存在)
     *
     * @param validateExistsDto          business dto param
     * @param constraintValidatorContext The ValidatorContext
     * @return boolean
     */
    boolean validateExists(GXValidateExistsDto validateExistsDto, ConstraintValidatorContext constraintValidatorContext);

    /**
     * 通过条件更新数据
     *
     * @param tableName    需要更新的表名
     * @param updateFields 需要更新的数据
     * @param condition    更新条件
     * @return 影响的行数
     */
    Integer updateFieldByCondition(String tableName, List<GXUpdateField<?>> updateFields, List<GXCondition<?>> condition);

    /**
     * 获取 Primary Key
     *
     * @param entity 实体对象
     * @return String
     */
    String getPrimaryKeyName(T entity);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKeyName();

    /**
     * 获取实体的表名字
     *
     * @param entity 实体对象
     * @return 实体表名字
     */
    String getTableName(T entity);

    /**
     * 通过泛型标识获取实体的表名字
     *
     * @return 数据库表名字
     */
    String getTableName();

    /**
     * 自定义获取数据 可以从缓存获取
     *
     * @param scene 场景值
     * @return 缓存数据
     */
    default Object getDataFromCache(String scene) {
        return null;
    }

    /**
     * 缓存数据操作
     *
     * @param data  需要缓存的数据
     * @param scene 场景值
     */
    default void setDataToCache(Object data, String scene) {
    }

    /**
     * 失效缓存数据
     *
     * @param scene 场景值
     */
    default void invalidateCacheData(String scene) {
    }
}
