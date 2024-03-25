package cn.maple.mongodb.datasource.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.ddd.repository.GXBaseRepository;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.GXValidateExistsDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.mongodb.datasource.dao.GXMongoDao;
import cn.maple.mongodb.datasource.model.GXMongoModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GXMongoRepository<T extends GXMongoModel, D extends GXMongoDao<T, ID>, ID extends Serializable> implements GXBaseRepository<T, ID> {
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
    public ID updateOrCreate(T entity, List<GXCondition<?>> condition) {
        return null;
    }

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity) {
        return updateOrCreate(entity, Collections.emptyList());
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    @Override
    public List<Dict> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
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
    public List<Dict> findByCondition(String tableName, List<GXCondition<?>> condition) {
        return findByCondition(tableName, condition, CollUtil.newHashSet("*"));
    }

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @param columns   需要查询的列名字
     * @return 列表
     */
    public List<Dict> findByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).tableNameAlias(tableName).columns(columns).condition(condition).build();
        return findByCondition(queryParamInnerDto);
    }

    /**
     * 根据条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return R 返回数据
     */
    @Override
    public Dict findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
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
    public Dict findOneByCondition(String tableName, List<GXCondition<?>> condition) {
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
    public Dict findOneByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns) {
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
    public Dict findOneById(String tableName, ID id, Set<String> columns) {
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
    public Dict findOneById(String tableName, ID id) {
        return null;
    }

    /**
     * 根据条件获取分页数据
     *
     * @param dbQueryParamInnerDto 条件查询
     * @return 分页数据
     */
    @Override
    public GXPaginationResDto<Dict> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
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
    public GXPaginationResDto<Dict> paginate(String tableName, Integer page, Integer pageSize, List<GXCondition<?>> condition, Set<String> columns) {
        return null;
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @param extraData 附加数据
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition, Dict extraData) {
        return deleteSoftCondition(tableName, CollUtil.newArrayList(), condition, extraData);
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName       表名
     * @param updateFieldList 软删除时需要同时更新的字段
     * @param condition       删除条件
     * @param extraData       额外数据
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(String tableName, List<GXUpdateField<?>> updateFieldList, List<GXCondition<?>> condition, Dict extraData) {
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
    public Integer deleteCondition(String tableName, List<GXCondition<?>> condition) {
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
    public boolean checkRecordIsExists(String tableName, List<GXCondition<?>> condition) {
        return false;
    }

    /**
     * 实现验证注解(返回true表示数据已经存在)
     *
     * @param validateExistsDto          business dto param
     * @param constraintValidatorContext The ValidatorContext
     * @return boolean
     */
    @Override
    public boolean validateExists(GXValidateExistsDto validateExistsDto, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }

    /**
     * 通过条件更新数据
     *
     * @param tableName    需要更新的表名
     * @param updateFields 需要更新的数据
     * @param condition    更新条件
     * @return 影响的行数
     */
    @Override
    public Integer updateFieldByCondition(String tableName, List<GXUpdateField<?>> updateFields, List<GXCondition<?>> condition) {
        return null;
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
