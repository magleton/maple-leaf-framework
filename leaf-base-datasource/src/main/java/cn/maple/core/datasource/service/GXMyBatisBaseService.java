package cn.maple.core.datasource.service;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.constant.GXDataSourceConstant;
import cn.maple.core.datasource.dao.GXMyBatisDao;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.datasource.repository.GXMyBatisRepository;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.inner.field.GXUpdateStrField;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseDBResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXBusinessService;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * 业务DB基础Service
 *
 * @param <P>  仓库对象类型
 * @param <M>  Mapper类型
 * @param <T>  实体类型
 * @param <D>  DAO类型
 * @param <R>  响应对象类型
 * @param <ID> 实体的主键ID类型
 * @author britton chen <britton@126.com>
 */
@SuppressWarnings("unused")
public interface GXMyBatisBaseService<P extends GXMyBatisRepository<M, T, D, R, ID>, M extends GXBaseMapper<T, R>, T extends GXMyBatisModel, D extends GXMyBatisDao<M, T, R, ID>, R extends GXBaseDBResDto, ID extends Serializable> extends GXBusinessService, GXValidateDBExistsService {
    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(Table<String, String, Object> condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return Integer
     */
    Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return Integer
     */
    Integer updateFieldByCondition(Dict data, Table<String, String, Object> condition);

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto searchReqDto);

    /**
     * 通过条件查询列表信息
     *
     * @param queryParamInnerDto 搜索条件
     * @return List
     */
    List<R> findByCondition(GXBaseQueryParamInnerDto queryParamInnerDto);

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @return List
     */
    List<R> findByCondition(String tableName, Set<String> columns, Table<String, String, Object> condition);

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return List
     */
    List<R> findByCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition);

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param extraData 额外数据
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition, Object extraData);

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param columns   需要查询的列
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition, Set<String> columns);

    /**
     * 通过条件查询列表信息
     *
     * @param tableName  表名字
     * @param condition  搜索条件
     * @param columns    需要查询的字段
     * @param orderField 排序字段
     * @param groupField 分组字段
     * @return List
     */
    List<R> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, Map<String, String> orderField, Set<String> groupField);

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @param groupField 分组字段
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition, Map<String, String> orderField, Set<String> groupField);

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition, Map<String, String> orderField);

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @param extraData 额外参数
     * @return 一条数据
     */
    R findOneByCondition(String tableName, Set<String> columns, Table<String, String, Object> condition, Object extraData);

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(String tableName, Set<String> columns, Table<String, String, Object> condition);

    /**
     * 通过条件获取一条数据
     *
     * @param searchReqDto 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(GXBaseQueryParamInnerDto searchReqDto);

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param extraData 额外参数
     * @return 一条数据
     */
    R findOneByCondition(Table<String, String, Object> condition, Object extraData);

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(Table<String, String, Object> condition);

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param columns   字段集合
     * @return 一条数据
     */
    R findOneByCondition(Table<String, String, Object> condition, Set<String> columns);

    /**
     * 获取一条记录的指定字段
     *
     * @param condition   条件
     * @param fieldName   字段名字
     * @param targetClazz 返回的类型
     * @return 指定的类型
     */
    <E> E findOneSingleFieldByCondition(List<GXCondition<?>> condition, String fieldName, Class<E> targetClazz);

    /**
     * 获取一条记录的指定字段
     *
     * @param condition   条件
     * @param fieldName   字段名字
     * @param targetClazz 返回的类型
     * @return 指定的类型
     */
    <E> E findOneSingleFieldByCondition(Table<String, String, Object> condition, String fieldName, Class<E> targetClazz);

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    ID updateOrCreate(T entity);

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
     * @param req         请求参数
     * @param condition   条件
     * @param copyOptions 复制可选项
     * @return ID
     */
    <Q extends GXBaseReqDto> ID updateOrCreate(Q req, Table<String, String, Object> condition, CopyOptions copyOptions);

    /**
     * 创建或者更新
     *
     * @param req         请求参数
     * @param copyOptions 复制可选项
     * @return ID
     */
    <Q extends GXBaseReqDto> ID updateOrCreate(Q req, CopyOptions copyOptions);

    /**
     * 创建或者更新
     *
     * @param req 请求参数
     * @return ID
     */
    <Q extends GXBaseReqDto> ID updateOrCreate(Q req);

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @param extraData     额外数据
     * @return 新数据ID
     */
    ID copyOneData(Table<String, String, Object> copyCondition, Dict replaceData, Dict extraData);

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @return 新数据ID
     */
    ID copyOneData(Table<String, String, Object> copyCondition, Dict replaceData);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftCondition(Table<String, String, Object> condition);

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 根据条件删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(Table<String, String, Object> condition);

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
    <E> List<E> findFieldByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz);

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition(condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
     * </pre>
     *
     * @param condition   查询条件
     * @param columns     字段名字集合
     * @param targetClazz 值的类型
     * @return 返回指定的类型的值对象
     */
    <E> List<E> findFieldByCondition(Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz);

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition(condition, CollUtil.newHashSet("nickname", "username"));}
     * </pre>
     *
     * @param condition 查询条件
     * @param columns   字段名字集合
     * @return 返回指定的类型的值对象
     */
    List<R> findFieldByCondition(Table<String, String, Object> condition, Set<String> columns);

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param convertMethodName  结果集转换函数名字
     * @param copyOptions        转换选项
     * @param params             参数
     * @return Collection
     */
    Collection<R> findByCallMapperMethod(String mapperMethodMethod, String convertMethodName, CopyOptions copyOptions, Object... params);

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param params             参数
     * @return Object
     */
    Collection<R> findByCallMapperMethod(String mapperMethodMethod, Object... params);

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param convertMethodName  结果集转换函数名字
     * @param copyOptions        转换选项
     * @param params             参数
     * @return Object
     */
    R findOneByCallMapperMethod(String mapperMethodMethod, String convertMethodName, CopyOptions copyOptions, Object... params);

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodName 需要调用的方法
     * @param params           参数
     * @return Object
     */
    R findOneByCallMapperMethod(String mapperMethodName, Object... params);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKeyName(T entity);

    /**
     * 获取表的名字
     *
     * @return String
     */
    String getTableName();

    /**
     * 将Table类型的条件转换为DB中的条件
     *
     * @param tableNameAlias 表别名
     * @param condition      原始条件
     * @return 转换后的条件
     */
    default List<GXCondition<?>> convertTableToCondition(String tableNameAlias, Table<String, String, Object> condition) {
        ArrayList<GXCondition<?>> conditions = new ArrayList<>();
        condition.rowMap().forEach((column, datum) -> datum.forEach((op, value) -> {
            Dict data = Dict.create().set("tableNameAlias", tableNameAlias).set("fieldName", column).set("value", value);
            Function<Dict, GXCondition<?>> function = GXDataSourceConstant.getFunction(op);
            if (Objects.isNull(function)) {
                throw new GXBusinessException(CharSequenceUtil.format("请完善{}类型数据转换器", op));
            }
            conditions.add(function.apply(data));
        }));
        return conditions;
    }

    /**
     * 将Dict类型的数据字段转换为DB类型的数据字段
     *
     * @param tableNameAlias 表别名
     * @param data           原始数据字段
     * @return DB数据字段
     */
    default List<GXUpdateField<?>> convertDictToUpdateField(String tableNameAlias, Dict data) {
        List<GXUpdateField<?>> fields = new ArrayList<>();
        data.forEach((k, v) -> new GXUpdateStrField(tableNameAlias, k, v.toString()));
        return fields;
    }
}
