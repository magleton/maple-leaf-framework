package cn.maple.core.framework.api;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.TypeUtil;
import cn.maple.core.framework.constant.GXDataSourceConstant;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.protocol.req.GXQueryParamReqProtocol;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseApiResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXBusinessService;
import cn.maple.core.framework.util.GXCommonUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * RPC基础调用类
 * 封装了常用的一些方法
 * {@code
 * public class TestServiceApiImpl extends GXBaseServeApiImpl<TestReqDto, TestApiResDto, Integer> implements TestServiceApi {
 * public TestServiceApiImpl() {
 * staticBindServeServiceClass(OrdersService.class);
 * }
 * }
 * }
 *
 * @param <Q>  请求对象
 * @param <R>  返回数据
 * @param <ID> 数据库字段常量
 */
public class GXBaseServeApiImpl<S extends GXBusinessService, Q extends GXBaseReqDto, R extends GXBaseApiResDto, ID extends Serializable> implements GXBaseServeApi<Q, R, ID> {
    /**
     * 服务类的Class 对象
     * 静态目标服务的类型
     * 该类型是一直存在的
     * 子类调用staticBindServeServiceClass方法进行设置
     */
    protected static final Map<String, Class<?>> STATIC_SERVE_SERVICE_CLASS_MAP = new ConcurrentHashMap<>();

    /**
     * 动态绑定目标服务的类型
     * 方法调用完成将移除
     * 可以在方法调用时进行设置
     */
    protected static final ThreadLocal<Class<?>> DYNAMIC_SERVE_SERVICE_CLASS_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 根据条件获取数据
     * {@code
     * HashBasedTable<String, String, Object> hashBasedTable = HashBasedTable.create();
     * hashBasedTable.put("name", GXBuilderConstant.STR_EQ, "jack");
     * List<TestApiResDto> byCondition = testServiceApi.findByCondition(hashBasedTable);
     * }
     *
     * @param condition 查询条件
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition) {
        List<R> rs = findByCondition(condition, Dict.create());
        Class<R> retClazz = getGenericClassType();
        return GXCommonUtils.convertSourceListToTargetList(rs, retClazz, null, null);
    }

    /**
     * 通过条件查询列表信息
     * {@code
     * HashBasedTable<String, String, Object> hashBasedTable = HashBasedTable.create();
     * hashBasedTable.put("name", GXBuilderConstant.STR_EQ, "jack");
     * Map<String , String> orderField = new HashMap<>;
     * orderField.put("name" , "DESC");
     * List<TestApiResDto> byCondition = testServiceApi.findByCondition(hashBasedTable,orderField);
     * }
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Map<String, String> orderField) {
        Class<R> retClazz = getGenericClassType();
        List<GXCondition<?>> conditionList = convertTableConditionToConditionExp(getTableName(), condition);
        Object rLst = callMethod("findByCondition", conditionList, orderField);
        if (Objects.nonNull(rLst)) {
            return GXCommonUtils.convertSourceListToTargetList((Collection<?>) rLst, retClazz, null, null);
        }
        return Collections.emptyList();
    }

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Object extraData) {
        Class<R> retClazz = getGenericClassType();
        Object rLst = callMethod("findByCondition", convertTableConditionToConditionExp(condition), extraData);
        if (Objects.nonNull(rLst)) {
            return GXCommonUtils.convertSourceListToTargetList((Collection<?>) rLst, retClazz, null, null);
        }
        return Collections.emptyList();
    }

    /**
     * 根据条件获取数据
     *
     * @param condition   查询条件
     * @param columns     需要查询的列
     * @param targetClazz 目标类型
     * @return List
     */
    @Override
    public <E> List<E> findFieldByCondition(Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz) {
        List<GXCondition<?>> conditions = convertTableConditionToConditionExp(condition);
        Object o = callMethod("findMultiFieldByCondition", conditions, columns, targetClazz);
        return Convert.convert(new TypeReference<List<E>>() {
        }, o);
    }

    /**
     * 根据条件获取一条数据
     *
     * @param condition 查询条件
     * @return R
     */
    @Override
    public R findOneByCondition(Table<String, String, Object> condition) {
        return findOneByCondition(condition, Dict.create());
    }

    /**
     * 根据条件获取一条数据
     *
     * @param condition 查询条件
     * @return R
     */
    @Override
    public R findOneByCondition(Table<String, String, Object> condition, Object extraData) {
        Class<R> retClazz = getGenericClassType();
        Object r = callMethod("findOneByCondition", convertTableConditionToConditionExp(condition), extraData);
        if (Objects.nonNull(r)) {
            return GXCommonUtils.convertSourceToTarget(r, retClazz, null, CopyOptions.create());
        }
        return null;
    }

    /**
     * 创建或者更新数据
     *
     * @param reqDto      请求参数
     * @param condition   更新条件
     * @param copyOptions 复制可选项
     * @return ID
     */
    @Override
    public ID updateOrCreate(Q reqDto, Table<String, String, Object> condition, CopyOptions copyOptions) {
        Object id = callMethod("updateOrCreate", reqDto, convertTableConditionToConditionExp(condition), copyOptions);
        if (Objects.nonNull(id)) {
            return (ID) id;
        }
        return null;
    }

    /**
     * 创建或者更新数据
     *
     * @param reqDto      请求参数
     * @param copyOptions 复制可选项
     * @return ID
     */
    @Override
    public ID updateOrCreate(Q reqDto, CopyOptions copyOptions) {
        return updateOrCreate(reqDto, HashBasedTable.create(), copyOptions);
    }

    /**
     * 创建或者更新数据
     *
     * @param reqDto 请求参数
     * @return ID
     */
    @Override
    public ID updateOrCreate(Q reqDto) {
        return updateOrCreate(reqDto, CopyOptions.create());
    }

    /**
     * 分页数据
     *
     * @param reqProtocol 查询条件
     * @param copyOptions 复制可选项
     * @return 分页对象
     */
    @Override
    public GXPaginationResDto<R> paginate(GXQueryParamReqProtocol reqProtocol, CopyOptions copyOptions) {
        GXBaseQueryParamInnerDto baseQueryParamInnerDto = GXCommonUtils.convertSourceToTarget(reqProtocol, GXBaseQueryParamInnerDto.class, null, copyOptions);
        if (CharSequenceUtil.isEmpty(baseQueryParamInnerDto.getTableName())) {
            baseQueryParamInnerDto.setTableName(getTableName());
        }
        Object paginate = callMethod("paginate", baseQueryParamInnerDto);
        if (Objects.nonNull(paginate)) {
            GXPaginationResDto<R> retPaginate = (GXPaginationResDto<R>) paginate;
            // XXXDBResDto
            List<?> records = retPaginate.getRecords();
            Class<R> retClazz = getGenericClassType();
            List<R> rs = GXCommonUtils.convertSourceListToTargetList(records, retClazz, null, copyOptions);
            retPaginate.setRecords(rs);
            return retPaginate;
        }
        return null;
    }

    /**
     * 分页数据
     *
     * @param reqProtocol 查询条件
     * @return 分页对象
     */
    @Override
    public GXPaginationResDto<R> paginate(GXQueryParamReqProtocol reqProtocol) {
        return paginate(reqProtocol, CopyOptions.create());
    }

    /**
     * 物理删除
     *
     * @param condition 删除条件
     * @return 删除行数
     */
    @Override
    public Integer deleteCondition(Table<String, String, Object> condition) {
        Object cnt = callMethod("deleteCondition", convertTableConditionToConditionExp(condition));
        if (Objects.nonNull(cnt)) {
            return (Integer) cnt;
        }
        return 0;
    }

    /**
     * 软删除
     *
     * @param condition 删除条件
     * @return 删除行数
     */
    @Override
    public Integer deleteSoftCondition(Table<String, String, Object> condition) {
        Object cnt = callMethod("deleteSoftCondition", convertTableConditionToConditionExp(condition));
        if (Objects.nonNull(cnt)) {
            return (Integer) cnt;
        }
        return 0;
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param updateFields 需要更新的数据
     * @param condition    更新条件
     * @return Integer
     */
    @Override
    public Integer updateFieldByCondition(List<GXUpdateField<?>> updateFields, Table<String, String, Object> condition) {
        List<GXCondition<?>> conditionList = convertTableConditionToConditionExp(condition);
        Object cnt = callMethod("updateFieldByCondition", updateFields, conditionList);
        if (Objects.nonNull(cnt)) {
            return (Integer) cnt;
        }
        return 0;
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(Table<String, String, Object> condition) {
        Object exists = callMethod("checkRecordIsExists", convertTableConditionToConditionExp(condition));
        return (Boolean) exists;
    }

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @param methodName  转换方法名字
     * @param copyOptions 转换的自定义项
     * @param extraData   额外数据
     * @return
     */
    @Override
    public <T> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions, Dict extraData) {
        return GXCommonUtils.convertSourceToTarget(reqDto, targetClass, methodName, copyOptions, extraData);
    }

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @param methodName  转换方法名字
     * @param copyOptions 转换的自定义项
     * @return T
     */
    @Override
    public <T> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions) {
        return sourceToTarget(reqDto, targetClass, methodName, copyOptions, Dict.create());
    }

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @return T
     */
    @Override
    public <T> T sourceToTarget(Q reqDto, Class<T> targetClass) {
        return sourceToTarget(reqDto, targetClass, null, CopyOptions.create());
    }

    /**
     * 设置服务类的Class对象
     *
     * @param serveServiceClass 服务类Class对象
     */
    @Override
    public void staticBindServeServiceClass(Class<?> serveServiceClass) {
        STATIC_SERVE_SERVICE_CLASS_MAP.put(getClass().getSimpleName(), serveServiceClass);
    }

    /**
     * 子类可以动态指定目标服务类型
     *
     * @return GXBaseServeApi
     */
    @Override
    public GXBaseServeApi<Q, R, ID> callBindTargetServeSericeClass(Class<?> targetServeServiceClass) {
        DYNAMIC_SERVE_SERVICE_CLASS_THREAD_LOCAL.set(targetServeServiceClass);
        return this;
    }

    /**
     * 调用指定类中的指定方法
     *
     * @param methodName 方法名字
     * @param params     参数列表
     * @return Object
     */
    @Override
    public Object callMethod(String methodName, Object... params) {
        Class<?> serveServiceClass = getServeServiceClass();
        if (Objects.nonNull(serveServiceClass)) {
            return GXCommonUtils.reflectCallObjectMethod(serveServiceClass, methodName, params);
        }
        return null;
    }

    /**
     * 获取底层服务类的Class
     *
     * @return Class 返回数据的类型
     */
    @Override
    public Class<?> getServeServiceClass() {
        Class<?> serveServiceClass = DYNAMIC_SERVE_SERVICE_CLASS_THREAD_LOCAL.get();
        if (Objects.nonNull(serveServiceClass)) {
            DYNAMIC_SERVE_SERVICE_CLASS_THREAD_LOCAL.remove();
        }
        if (Objects.isNull(serveServiceClass)) {
            serveServiceClass = STATIC_SERVE_SERVICE_CLASS_MAP.get(getClass().getSimpleName());
        }
        return serveServiceClass;
    }

    /**
     * 获取返回的Class
     *
     * @return Class 返回数据的类型
     */
    @Override
    public Class<R> getGenericClassType() {
        return (Class<R>) TypeUtil.getClass(((ParameterizedType) getClass().getInterfaces()[0].getGenericInterfaces()[0]).getActualTypeArguments()[1]);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<GXCondition<?>> convertTableConditionToConditionExp(Table<String, String, Object> condition) {
        return convertTableConditionToConditionExp(getTableName(), condition);
    }

    /**
     * 将Table类型的条件转换为条件表达式
     *
     * @param tableNameAlias 表别名
     * @param condition      原始条件
     * @return 转换后的条件
     */
    @Override
    public List<GXCondition<?>> convertTableConditionToConditionExp(String tableNameAlias, Table<String, String, Object> condition) {
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
     * 获取表的名字
     *
     * @return String 表名字
     */
    @Override
    public String getTableName() {
        return (String) callMethod("getTableName");
    }
}
