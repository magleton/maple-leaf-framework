package cn.maple.mongodb.datasource.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.ddd.repository.GXBaseRepository;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.mongodb.datasource.dao.GXMongoDao;
import cn.maple.mongodb.datasource.model.GXMongoModel;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GXMongoRepository<T extends GXMongoModel, D extends GXMongoDao<T, R, ID>, R extends GXBaseResDto, ID extends Serializable> implements GXBaseRepository<T, R, ID> {
    /**
     * 基础DAO
     */
    @SuppressWarnings("all")
    @Autowired
    protected D baseDao;

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        return baseDao.updateOrCreate(entity, condition);
    }

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity) {
        return updateOrCreate(entity, HashBasedTable.create());
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @param targetClazz     目标对象
     * @param extraData       自定义填充数据
     * @return 列表
     */
    @Override
    public <E> List<E> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz, Dict extraData) {
        return Collections.emptyList();
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @param targetClazz     目标对象类型
     * @return 列表
     */
    @Override
    public <E> List<E> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz) {
        return findByCondition(dbQueryInnerDto, targetClazz, Dict.create());
    }

    /**
     * 根据条件获取所有数据
     *
     * @param tableName   表名字
     * @param condition   查询条件
     * @param columns     查询列
     * @param targetClazz 结果数据类型
     * @return 列表
     */
    @Override
    public <E> List<E> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz) {
        return Collections.emptyList();
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto) {
        return Collections.emptyList();
    }

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition) {
        return Collections.emptyList();
    }

    /**
     * 根据条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return R 返回数据
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return null;
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return R 返回数据
     */
    @Override
    public R findOneByCondition(String tableName, Table<String, String, Object> condition) {
        return null;
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @param columns   需要查询的列
     * @return R 返回数据
     */
    @Override
    public R findOneByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns) {
        return null;
    }

    /**
     * 通过ID获取一条记录
     *
     * @param tableName 表名字
     * @param id        ID值
     * @param columns   需要返回的列
     * @return 返回数据
     */
    @Override
    public R findOneById(String tableName, ID id, Set<String> columns) {
        return null;
    }

    /**
     * 通过ID获取一条记录
     *
     * @param tableName 表名字
     * @param id        ID值
     * @return 返回数据
     */
    @Override
    public R findOneById(String tableName, ID id) {
        return null;
    }

    /**
     * 根据条件获取分页数据
     *
     * @param dbQueryParamInnerDto 条件查询
     * @return 分页数据
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return null;
    }

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
    @Override
    public GXPaginationResDto<R> paginate(String tableName, Integer page, Integer pageSize, Table<String, String, Object> condition, Set<String> columns) {
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
     * 检测数据是否存在
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 存在 0 不存在
     */
    @Override
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        return false;
    }

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
    @Override
    public boolean validateExists(Object value, String tableName, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        return false;
    }

    /**
     * 通过条件更新数据
     *
     * @param tableName 需要更新的表名
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 影响的行数
     */
    @Override
    public Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        return null;
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code
     *     findFieldByCondition("s_admin", condition, CollUtil.newHashSet("nickname", "username"), Dict.class);
     *     }
     * </pre>
     *
     * @param tableName   表名字
     * @param condition   查询条件
     * @param columns     字段名字集合
     * @param targetClazz 值的类型
     * @return 返回指定的类型的值对象
     */
    @Override
    public <E> E findFieldByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz) {
        return null;
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code
     *     GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder()
     *                       .tableName("s_admin")
     *                       .columns(CollUtil.newHashSet("nickname", "username"))
     *                       .condition(condition)
     *                       .build();
     *     findFieldByCondition(paramInnerDto, Dict.class);
     *     }
     * </pre>
     *
     * @param dbQueryInnerDto 查询数据
     * @param targetClazz     值的类型
     * @param extraData       自定义填充数据
     * @return 返回指定的类型的值对象
     */
    @Override
    public <E> E findFieldByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz, Dict extraData) {
        return null;
    }

    /**
     * 通过条件获取单字段的数据
     * <pre>
     * {@code
     * HashBasedTable<String, String, Object> condition = HashBasedTable.create();
     * String username = getSingleField("s_admin" ,condition , "username" , String.class);
     * }
     * </pre>
     *
     * @param tableName   表名
     * @param condition   查询条件
     * @param fieldName   需要的字段名字
     * @param targetClazz 返回的字段类型
     * @return 目标类型的值
     */
    @Override
    public <E> E getSingleField(String tableName, Table<String, String, Object> condition, String fieldName, Class<E> targetClazz) {
        return findFieldByCondition(tableName, condition, CollUtil.newHashSet(fieldName), targetClazz);
    }

    /**
     * 获取 Primary Key
     *
     * @param entity 实体对象
     * @return String
     */
    @Override
    public String getPrimaryKeyName(T entity) {
        return "id";
    }

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    @Override
    public String getPrimaryKeyName() {
        return null;
    }

    /**
     * 获取实体的表名字
     *
     * @param entity 实体对象
     * @return 实体表名字
     */
    @Override
    public String getTableName(T entity) {
        return null;
    }

    /**
     * 通过泛型标识获取实体的表名字
     *
     * @return 数据库表名字
     */
    @Override
    public String getTableName() {
        return null;
    }
}
