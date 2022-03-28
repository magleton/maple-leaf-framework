package cn.maple.core.datasource.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.datasource.dao.GXMyBatisDao;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.datasource.repository.GXMyBatisRepository;
import cn.maple.core.datasource.service.GXMyBatisBaseService;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.exception.GXDBNotExistsException;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 业务基础Service
 *
 * @param <P>  仓库对象类型
 * @param <M>  Mapper类型
 * @param <T>  实体类型
 * @param <D>  DAO类型
 * @param <R>  响应对象类型
 * @param <ID> 实体的主键ID类型
 */
public class GXMyBatisBaseServiceImpl<P extends GXMyBatisRepository<M, T, D, R, ID>, M extends GXBaseMapper<T, R>, T extends GXMyBatisModel, D extends GXMyBatisDao<M, T, R, ID>, R extends GXBaseResDto, ID extends Serializable> extends GXBusinessServiceImpl implements GXMyBatisBaseService<P, M, T, D, R, ID> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXMyBatisBaseServiceImpl.class);

    /**
     * 仓库类型
     */
    @Autowired
    @SuppressWarnings("all")
    protected P repository;

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        return repository.checkRecordIsExists(tableName, condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(Table<String, String, Object> condition) {
        return checkRecordIsExists(repository.getTableName(), condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return Integer
     */
    @Override
    public Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        return repository.updateFieldByCondition(tableName, data, condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return Integer
     */
    @Override
    public Integer updateFieldByCondition(Dict data, Table<String, String, Object> condition) {
        return updateFieldByCondition(repository.getTableName(), data, condition);
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名
     * @param dataList  数据集合
     * @return int
     */
    @SuppressWarnings("all")
    @Override
    public Integer batchSave(String tableName, List<Dict> dataList) {
        return repository.batchSave(tableName, dataList);
    }

    /**
     * 批量插入数据
     *
     * @param dataList 待插入的数据
     * @return 插入的行数
     */
    @Override
    public Integer batchSave(List<Dict> dataList) {
        return batchSave(repository.getTableName(), dataList);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param queryParamReqDto 参数
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto queryParamReqDto) {
        if (Objects.isNull(queryParamReqDto.getColumns())) {
            queryParamReqDto.setColumns(CollUtil.newHashSet("*"));
        }
        return repository.paginate("paginate", queryParamReqDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param searchReqDto 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto searchReqDto) {
        String tableName = searchReqDto.getTableName();
        if (CharSequenceUtil.isBlank(tableName)) {
            tableName = repository.getTableName();
            searchReqDto.setTableName(tableName);
        }
        return repository.findByCondition(searchReqDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param tableName            表名字
     * @param columns              需要查询的字段
     * @param condition            搜索条件
     * @param gainAssociatedFields 需要获取关联数据的字段名字集合
     * @return List
     */
    @Override
    public List<R> findByCondition(String tableName, Set<String> columns, Table<String, String, Object> condition, List<String> gainAssociatedFields) {
        return findByCondition(tableName, condition, columns, gainAssociatedFields, null, null);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param tableName            表名字
     * @param condition            搜索条件
     * @param gainAssociatedFields 需要获取关联数据的字段名字集合
     * @return List
     */
    @Override
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition, List<String> gainAssociatedFields) {
        return findByCondition(tableName, CollUtil.newHashSet("*"), condition, gainAssociatedFields);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition) {
        return findByCondition(tableName, CollUtil.newHashSet("*"), condition, null);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition            搜索条件
     * @param gainAssociatedFields 需要获取关联数据的字段名字集合
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, List<String> gainAssociatedFields) {
        return findByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition, gainAssociatedFields);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition) {
        return findByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition, null);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param columns   需要查询的列
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Set<String> columns) {
        return findByCondition(repository.getTableName(), columns, condition, null);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param tableName            表名字
     * @param condition            搜索条件
     * @param columns              需要查询的字段
     * @param gainAssociatedFields 需要获取关联数据的字段名字集合
     * @param orderField           排序字段
     * @param groupField           分组字段
     * @return List
     */
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, List<String> gainAssociatedFields, Dict orderField, Set<String> groupField) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).gainAssociatedFields(gainAssociatedFields).condition(condition).orderByField(orderField).groupByField(groupField).build();
        return findByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @param groupField 分组字段
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Dict orderField, Set<String> groupField) {
        return findByCondition(repository.getTableName(), condition, CollUtil.newHashSet("*"), null, orderField, groupField);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Dict orderField) {
        return findByCondition(condition, orderField, null);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param searchReqDto 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto searchReqDto) {
        return repository.findOneByCondition(searchReqDto);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName            表名字
     * @param columns              需要查询的字段
     * @param condition            搜索条件
     * @param gainAssociatedFields 需要获取关联数据的字段名字集合
     * @return 一条数据
     */
    public R findOneByCondition(String tableName, Set<String> columns, Table<String, String, Object> condition, List<String> gainAssociatedFields) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(condition).gainAssociatedFields(gainAssociatedFields).build();
        return findOneByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(String tableName, Table<String, String, Object> condition) {
        return findOneByCondition(tableName, CollUtil.newHashSet("*"), condition, null);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName            表名字
     * @param condition            搜索条件
     * @param gainAssociatedFields 需要获取关联数据的字段名字集合
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(String tableName, Table<String, String, Object> condition, List<String> gainAssociatedFields) {
        return findOneByCondition(tableName, CollUtil.newHashSet("*"), condition, gainAssociatedFields);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(Table<String, String, Object> condition) {
        return findOneByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition, null);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param columns   字段集合
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(Table<String, String, Object> condition, Set<String> columns) {
        return findOneByCondition(repository.getTableName(), columns, condition, null);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition            搜索条件
     * @param gainAssociatedFields 需要获取关联数据的字段名字集合
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(Table<String, String, Object> condition, List<String> gainAssociatedFields) {
        return findOneByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition, gainAssociatedFields);
    }

    /**
     * 创建数据
     *
     * @param entity 数据实体
     * @return ID
     */
    @Override
    public ID create(T entity) {
        return repository.create(entity);
    }

    /**
     * 更新数据
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    @Override
    public ID update(T entity, Table<String, String, Object> condition) {
        return repository.update(entity, condition);
    }

    /**
     * 更新数据
     *
     * @param entity        数据实体
     * @param updateWrapper 更新条件
     * @return ID
     */
    @Override
    public ID update(T entity, UpdateWrapper<T> updateWrapper) {
        return repository.update(entity, updateWrapper);
    }

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    @SuppressWarnings("all")
    @Override
    public ID updateOrCreate(T entity) {
        return repository.updateOrCreate(entity);
    }

    /**
     * 创建或者更新
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        return repository.updateOrCreate(entity, condition);
    }

    /**
     * 创建或者更新
     *
     * @param entity        数据实体
     * @param updateWrapper 更新条件
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, UpdateWrapper<T> updateWrapper) {
        return repository.updateOrCreate(entity, updateWrapper);
    }

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @param customerData  额外数据
     * @return 新数据ID
     */
    @Override
    public ID copyOneData(Table<String, String, Object> copyCondition, Dict replaceData, Object... customerData) {
        R oneData = findOneByCondition(repository.getTableName(), copyCondition);
        if (Objects.isNull(oneData)) {
            throw new GXDBNotExistsException("待拷贝的数据不存在!!");
        }
        T entity = GXCommonUtils.convertSourceToTarget(oneData, getModelClass(), null, null, customerData);
        assert entity != null;
        String setPrimaryKeyMethodName = CharSequenceUtil.format("set{}", CharSequenceUtil.upperFirst(getPrimaryKeyName(entity)));
        Method method = ReflectUtil.getMethod(entity.getClass(), setPrimaryKeyMethodName, getIDClassType());
        if (Objects.isNull(method)) {
            throw new GXBusinessException(CharSequenceUtil.format("方法{}不存在", setPrimaryKeyMethodName));
        }
        ReflectUtil.invoke(entity, method, (Object) null);
        replaceData.forEach((k, v) -> GXCommonUtils.reflectCallObjectMethod(entity, CharSequenceUtil.format("set{}", CharSequenceUtil.upperFirst(CharSequenceUtil.toCamelCase(k))), v));

        return create(entity);
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
        return repository.deleteSoftCondition(tableName, condition);
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(Table<String, String, Object> condition) {
        return deleteSoftCondition(repository.getTableName(), condition);
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
        return repository.deleteCondition(tableName, condition);
    }

    /**
     * 根据条件删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteCondition(Table<String, String, Object> condition) {
        return deleteCondition(repository.getTableName(), condition);
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition("s_admin", condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
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
        return repository.findFieldByCondition(tableName, condition, columns, targetClazz);
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition("s_admin", condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
     * </pre>
     *
     * @param condition   查询条件
     * @param columns     字段名字集合
     * @param targetClazz 值的类型
     * @return 返回指定的类型的值对象
     */
    @Override
    public <E> E findFieldByCondition(Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz) {
        return findFieldByCondition(repository.getTableName(), condition, columns, targetClazz);
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition("s_admin", condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
     * </pre>
     *
     * @param condition 查询条件
     * @param columns   字段名字集合
     * @return 返回指定的类型的值对象
     */
    @Override
    public R findFieldByCondition(Table<String, String, Object> condition, Set<String> columns) {
        return findFieldByCondition(repository.getTableName(), condition, columns, getReturnValueType());
    }

    /**
     * 获取实体类型的Class
     *
     * @return 实体类的Class
     */
    @Override
    public Class<T> getModelClass() {
        return repository.getModelClass();
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
        if (CharSequenceUtil.isBlank(tableName)) {
            tableName = repository.getTableName();
        }
        if (CharSequenceUtil.isBlank(fieldName)) {
            fieldName = repository.getPrimaryKeyName();
        }
        return repository.validateExists(value, tableName, fieldName, constraintValidatorContext, param);
    }

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    @Override
    public String getPrimaryKeyName(T entity) {
        return repository.getPrimaryKeyName(entity);
    }

    /**
     * 获取返回值的类型
     *
     * @return Class
     */
    @Override
    @SuppressWarnings("all")
    public Class<R> getReturnValueType() {
        return (Class<R>) repository.getReturnValueType();
    }

    /**
     * 获取主键标识的类型
     *
     * @return Class
     */
    @Override
    public Class<ID> getIDClassType() {
        return repository.getIDClassType();
    }
}
