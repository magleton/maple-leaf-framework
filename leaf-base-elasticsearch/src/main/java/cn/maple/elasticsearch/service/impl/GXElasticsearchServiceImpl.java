package cn.maple.elasticsearch.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.GXUnionTypeEnums;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseDBResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.elasticsearch.dao.GXElasticsearchDao;
import cn.maple.elasticsearch.model.GXElasticsearchModel;
import cn.maple.elasticsearch.repository.GXElasticsearchRepository;
import cn.maple.elasticsearch.service.GXElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.BaseQuery;
import org.springframework.data.elasticsearch.core.query.BaseQueryBuilder;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class GXElasticsearchServiceImpl<P extends GXElasticsearchRepository<T, D, Q, B, ID>, T extends GXElasticsearchModel, D extends GXElasticsearchDao<T, Q, B, ID>, Q extends BaseQuery, B extends BaseQueryBuilder<Q, B>, R extends GXBaseDBResDto, ID extends Serializable> extends GXBusinessServiceImpl implements GXElasticsearchService<P, T, D, Q, B, R, ID> {
    /**
     * 日志对象
     */
    @SuppressWarnings("all")
    private static final Logger LOGGER = LoggerFactory.getLogger(GXElasticsearchServiceImpl.class);

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
    public boolean checkRecordIsExists(String tableName, List<GXCondition<?>> condition) {
        return false;
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(List<GXCondition<?>> condition) {
        return checkRecordIsExists("", condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName    表名字
     * @param updateFields 需要更新的数据
     * @param condition    更新条件
     * @return Integer
     */
    @Override
    public Integer updateFieldByCondition(String tableName, List<GXUpdateField<?>> updateFields, List<GXCondition<?>> condition) {
        return null;
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param updateFields 需要更新的数据
     * @param condition    更新条件
     * @return Integer
     */
    @Override
    public Integer updateFieldByCondition(List<GXUpdateField<?>> updateFields, List<GXCondition<?>> condition) {
        return null;
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param queryParamInnerDto 参数
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto queryParamInnerDto) {
        CopyOptions copyOptions = getCopyOptions(queryParamInnerDto);
        Class<R> genericClassType = GXCommonUtils.getGenericClassType(getClass(), 5);
        GXPaginationResDto<Dict> paginate = repository.paginate(queryParamInnerDto);
        List<R> lst = paginate.getRecords().stream().map(dict -> {
            Object extraData = Optional.ofNullable(queryParamInnerDto.getExtraData()).orElse(Dict.create());
            return GXCommonUtils.convertSourceToTarget(dict, genericClassType, queryParamInnerDto.getMethodName(), copyOptions, extraData);
        }).collect(Collectors.toList());
        return new GXPaginationResDto<>(lst, paginate.getTotal(), paginate.getPageSize(), paginate.getCurrentPage());
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums) {
        LOGGER.error("请自己实现此方法");
        return null;
    }

    /**
     * 通过条件查询列表信息
     *
     * @param queryParamInnerDto 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto queryParamInnerDto) {
        CopyOptions copyOptions = getCopyOptions(queryParamInnerDto);
        String[] methodName = new String[]{queryParamInnerDto.getMethodName()};
        if (CharSequenceUtil.isEmpty(methodName[0])) {
            methodName[0] = GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME;
        }
        Class<R> genericClassType = GXCommonUtils.getGenericClassType(getClass(), 5);
        Function<Dict, R> rowMapper = dict -> {
            Object extraData = Optional.ofNullable(queryParamInnerDto.getExtraData()).orElse(Dict.create());
            return GXCommonUtils.convertSourceToTarget(dict, genericClassType, methodName[0], copyOptions, extraData);
        };
        return findByCondition(queryParamInnerDto, rowMapper);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return List
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums) {
        LOGGER.error("请自己实现该方法");
        return null;
    }

    /**
     * 通过条件查询列表信息
     *
     * @param queryParamInnerDto 查询条件
     * @param rowMapper          映射函数
     * @return List
     */
    @Override
    public <E> List<E> findByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Function<Dict, E> rowMapper) {
        String tableName = queryParamInnerDto.getTableName();
        if (CharSequenceUtil.isBlank(tableName)) {
            tableName = repository.getTableName();
            queryParamInnerDto.setTableName(tableName);
        }
        List<Dict> list = repository.findByCondition(queryParamInnerDto);
        return list.stream().map(rowMapper).collect(Collectors.toList());
    }

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(String tableName, Set<String> columns, List<GXCondition<?>> condition) {
        return findByCondition(tableName, condition, columns, null, null);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(String tableName, List<GXCondition<?>> condition) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(CollUtil.newHashSet("*")).condition(condition).build();
        return findByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(List<GXCondition<?>> condition) {
        return findByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param extraData 额外数据
     * @return List
     */
    @Override
    public List<R> findByCondition(List<GXCondition<?>> condition, Object extraData) {
        HashSet<String> columns = CollUtil.newHashSet("*");
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(repository.getTableName()).columns(columns).condition(condition).extraData(extraData).build();
        return findByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param columns   需要查询的列
     * @return List
     */
    @Override
    public List<R> findByCondition(List<GXCondition<?>> condition, Set<String> columns) {
        return findByCondition(repository.getTableName(), columns, condition);
    }

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
    @Override
    public List<R> findByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Map<String, String> orderField, Set<String> groupField) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(condition).orderByField(orderField).groupByField(groupField).build();
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
    public List<R> findByCondition(List<GXCondition<?>> condition, Map<String, String> orderField, Set<String> groupField) {
        return findByCondition(repository.getTableName(), condition, CollUtil.newHashSet("*"), orderField, groupField);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @return List
     */
    @Override
    public List<R> findByCondition(List<GXCondition<?>> condition, Map<String, String> orderField) {
        return findByCondition(condition, orderField, null);
    }

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
    @Override
    public R findOneByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Map<String, String> orderField, Set<String> groupField) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(condition).orderByField(orderField).groupByField(groupField).build();
        return findOneByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @param extraData 额外参数
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(String tableName, Set<String> columns, List<GXCondition<?>> condition, Object extraData) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(condition).extraData(extraData).build();
        return findOneByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(String tableName, Set<String> columns, List<GXCondition<?>> condition) {
        return findOneByCondition(tableName, columns, condition, Dict.create());
    }

    /**
     * 通过条件获取一条数据
     *
     * @param queryParamInnerDto 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto queryParamInnerDto) {
        String[] methodName = new String[]{queryParamInnerDto.getMethodName()};
        if (CharSequenceUtil.isEmpty(methodName[0])) {
            methodName[0] = GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME;
        }
        Object extraData = Optional.ofNullable(queryParamInnerDto.getExtraData()).orElse(Dict.class);
        CopyOptions copyOptions = getCopyOptions(queryParamInnerDto);
        Class<R> genericClassType = GXCommonUtils.getGenericClassType(getClass(), 5);
        Function<Dict, R> rowMapper = dict -> {
            return GXCommonUtils.convertSourceToTarget(dict, genericClassType, methodName[0], copyOptions, extraData);
        };
        return findOneByCondition(queryParamInnerDto, rowMapper);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums) {
        LOGGER.error("请自己实现此方法");
        return null;
    }

    /**
     * 通过条件获取一条数据
     *
     * @param queryParamInnerDto 搜索条件
     * @param rowMapper          映射函数
     * @return 一条数据
     */
    @Override
    public <E> E findOneByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Function<Dict, E> rowMapper) {
        Dict dict = repository.findOneByCondition(queryParamInnerDto);
        if (Objects.isNull(dict)) {
            return null;
        }
        return rowMapper.apply(dict);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(String tableName, List<GXCondition<?>> condition) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(CollUtil.newHashSet("*")).condition(condition).extraData(Dict.create()).build();
        return findOneByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param extraData 额外参数
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(List<GXCondition<?>> condition, Object extraData) {
        return findOneByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition, extraData);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(List<GXCondition<?>> condition) {
        return findOneByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param columns   字段集合
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(List<GXCondition<?>> condition, Set<String> columns) {
        return findOneByCondition(repository.getTableName(), columns, condition);
    }

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity) {
        return repository.updateOrCreate(entity, Collections.emptyList());
    }

    /**
     * 创建或者更新
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, List<GXCondition<?>> condition) {
        return repository.updateOrCreate(entity, condition);
    }

    /**
     * 创建或者更新
     *
     * @param req         请求参数
     * @param condition   条件
     * @param copyOptions 复制可选项
     * @return ID
     */
    @Override
    public <Q extends GXBaseReqDto> ID updateOrCreate(Q req, List<GXCondition<?>> condition, CopyOptions copyOptions) {
        Class<T> targetClazz = GXCommonUtils.getGenericClassType(getClass(), 2);
        T entity = convertSourceToTarget(req, targetClazz, GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME, copyOptions);
        return updateOrCreate(entity, condition);
    }

    /**
     * 创建或者更新
     *
     * @param req         请求参数
     * @param copyOptions 复制可选项
     * @return ID
     */
    @Override
    public <Q extends GXBaseReqDto> ID updateOrCreate(Q req, CopyOptions copyOptions) {
        return updateOrCreate(req, Collections.emptyList(), copyOptions);
    }

    /**
     * 创建或者更新
     *
     * @param req 请求参数
     * @return ID
     */
    @Override
    public <Q extends GXBaseReqDto> ID updateOrCreate(Q req) {
        return updateOrCreate(req, CopyOptions.create());
    }

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @param extraData     额外数据
     * @return 新数据ID
     */
    @Override
    public ID copyOneData(List<GXCondition<?>> copyCondition, Dict replaceData, Dict extraData) {
        return null;
    }

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @return 新数据ID
     */
    @Override
    public ID copyOneData(List<GXCondition<?>> copyCondition, Dict replaceData) {
        return null;
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @param extraData 额外数据
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition, Dict extraData) {
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
    public Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition) {
        return null;
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(List<GXCondition<?>> condition) {
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
        return repository.deleteCondition(tableName, condition);
    }

    /**
     * 根据条件删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteCondition(List<GXCondition<?>> condition) {
        return deleteCondition(repository.getTableName(), condition);
    }

    /**
     * 根据ID删除数据
     *
     * @param id ID
     * @return 删除的条数
     */
    @Override
    public Integer deleteById(ID id) {
        return repository.deleteById(id);
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
    public <E> List<E> findMultiFieldByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Class<E> targetClazz) {
        return null;
    }

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
    @Override
    public <E> List<E> findMultiFieldByCondition(List<GXCondition<?>> condition, Set<String> columns, Class<E> targetClazz) {
        return null;
    }

    /**
     * 查询指定字段的值
     *
     * @param queryParamInnerDto 查询参数
     * @param targetClazz        返回数据的类型
     * @return 返回指定的类型的值对象
     */
    @Override
    public <E> List<E> findMultiFieldByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Class<E> targetClazz) {
        return null;
    }

    /**
     * 获取一条记录的指定单字段
     *
     * @param queryParamInnerDto 查询条件
     * @param targetClazz        返回的类型
     * @return 指定的类型
     */
    @Override
    public <E> E findSingleFieldByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Class<E> targetClazz) {
        return null;
    }

    /**
     * 获取指定单字段的列表
     *
     * @param queryParamInnerDto 条件
     * @param targetClazz        目标类型
     * @return 指定的类型
     */
    @Override
    public <E> List<E> findSingleFieldLstByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Class<E> targetClazz) {
        return null;
    }

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param convertMethodName  结果集转换函数名字
     * @param copyOptions        转换选项
     * @param params             参数
     * @return Collection
     */
    @Override
    public Collection<R> findByCallMapperMethod(String mapperMethodMethod, String convertMethodName, CopyOptions copyOptions, Object... params) {
        return null;
    }

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param params             参数
     * @return Object
     */
    @Override
    public Collection<R> findByCallMapperMethod(String mapperMethodMethod, Object... params) {
        return null;
    }

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param convertMethodName  结果集转换函数名字
     * @param copyOptions        转换选项
     * @param params             参数
     * @return Object
     */
    @Override
    public R findOneByCallMapperMethod(String mapperMethodMethod, String convertMethodName, CopyOptions copyOptions, Object... params) {
        return null;
    }

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodName 需要调用的方法
     * @param params           参数
     * @return Object
     */
    @Override
    public R findOneByCallMapperMethod(String mapperMethodName, Object... params) {
        return null;
    }

    /**
     * 根据条件统计数量
     *
     * @param conditions 查询条件
     * @return 查询到的数量
     */
    @Override
    public Long countByCondition(List<GXCondition<?>> conditions) {
        return null;
    }

    /**
     * 根据条件统计数量
     *
     * @param queryParamInnerDto 查询条件
     * @return 查询到的数量
     */
    @Override
    public Long countByCondition(GXBaseQueryParamInnerDto queryParamInnerDto) {
        return null;
    }

    /**
     * 获取 Primary Key
     *
     * @param entity
     * @return String
     */
    @Override
    public String getPrimaryKeyName(T entity) {
        return null;
    }

    /**
     * 获取表的名字
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return null;
    }
}
