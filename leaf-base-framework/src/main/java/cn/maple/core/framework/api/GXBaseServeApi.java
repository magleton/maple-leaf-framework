package cn.maple.core.framework.api;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.protocol.req.GXQueryParamReqProtocol;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseApiResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.util.GXCommonUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 暴露服务的基础API接口
 *
 * @param <Q>  请求参数的类型
 * @param <R>  响应对象的类型
 * @param <ID> 唯一标识的类型  一般是一个实体对象的ID类型
 */
@SuppressWarnings("all")
public interface GXBaseServeApi<Q extends GXBaseReqDto, R extends GXBaseApiResDto, ID extends Serializable> {
    /**
     * 服务类的Class 对象
     */
    Map<String, Class<?>> serveServiceClassMap = new ConcurrentHashMap<>();

    /**
     * 目标服务的类型
     */
    ThreadLocal<Class<?>> targetServeServiceClassThreadLocal = new ThreadLocal<>();

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    default List<R> findByCondition(Table<String, String, Object> condition) {
        List<R> rs = findByCondition(condition, Dict.create());
        Class<R> retClazz = GXCommonUtils.getGenericClassType(getClass(), 1);
        return GXCommonUtils.convertSourceListToTargetList(rs, retClazz, null, null);
    }

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    default List<R> findByCondition(Table<String, String, Object> condition, Object extraData) {
        Class<R> retClazz = GXCommonUtils.getGenericClassType(getClass(), 1);
        Object rLst = callMethod("findByCondition", condition, extraData);
        if (Objects.nonNull(rLst)) {
            return GXCommonUtils.convertSourceListToTargetList((Collection<?>) rLst, retClazz, null, null);
        }
        return Collections.emptyList();
    }

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @param columns   需要查询的列
     * @param extraData 额外数据
     * @return List
     */
    default <E> List<E> findByCondition(Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz, Dict extraData) {
        List<R> rList = (List<R>) callMethod("findByCondition", condition, columns);
        return GXCommonUtils.convertSourceListToTargetList(rList, targetClazz, null, null, extraData);
    }

    /**
     * 根据条件获取一条数据
     *
     * @param condition   查询条件
     * @param columns     需要查询的列
     * @param targetClazz 目标类型
     * @param extraData   额外数据
     * @return R
     */
    default <E> E findOneByCondition(Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz, Dict extraData) {
        Object data = callMethod("findOneByCondition", condition, columns);
        return GXCommonUtils.convertSourceToTarget(data, targetClazz, null, null, extraData);
    }

    /**
     * 根据条件获取一条数据
     *
     * @param condition 查询条件
     * @return R
     */
    default R findOneByCondition(Table<String, String, Object> condition) {
        return findOneByCondition(condition, Dict.create());
    }

    /**
     * 根据条件获取一条数据
     *
     * @param condition 查询条件
     * @return R
     */
    default R findOneByCondition(Table<String, String, Object> condition, Object extraData) {
        Object r = callMethod("findOneByCondition", condition, extraData);
        Class<R> retClazz = GXCommonUtils.getGenericClassType(getClass(), 1);
        if (Objects.nonNull(r)) {
            return GXCommonUtils.convertSourceToTarget(r, retClazz, null, null);
        }
        return null;
    }

    /**
     * 创建或者更新数据
     *
     * @param reqDto    请求参数
     * @param condition 更新条件
     * @return ID
     */
    default ID updateOrCreate(Q reqDto, Table<String, String, Object> condition) {
        Object id = callMethod("updateOrCreate", reqDto, condition);
        if (Objects.nonNull(id)) {
            return (ID) id;
        }
        return null;
    }

    /**
     * 创建或者更新数据
     *
     * @param reqDto 请求参数
     * @return ID
     */
    default ID updateOrCreate(Q reqDto) {
        return updateOrCreate(reqDto, HashBasedTable.create());
    }

    /**
     * 分页数据
     *
     * @param reqProtocol 查询条件
     * @return 分页对象
     */
    default GXPaginationResDto<R> paginate(GXQueryParamReqProtocol reqProtocol) {
        Object paginate = callMethod("paginate", reqProtocol);
        if (Objects.nonNull(paginate)) {
            GXPaginationResDto<R> retPaginate = (GXPaginationResDto<R>) paginate;
            List<R> records = retPaginate.getRecords();
            Class<R> retClazz = GXCommonUtils.getGenericClassType(getClass(), 1);
            List<R> rs = GXCommonUtils.convertSourceListToTargetList(records, retClazz, null, null);
            retPaginate.setRecords(records);
            return retPaginate;
        }
        return null;
    }

    /**
     * 物理删除
     *
     * @param condition 删除条件
     * @return 删除行数
     */
    default Integer deleteCondition(Table<String, String, Object> condition) {
        Object cnt = callMethod("deleteCondition", condition);
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
    default Integer deleteSoftCondition(Table<String, String, Object> condition) {
        Object cnt = callMethod("deleteSoftCondition", condition);
        if (Objects.nonNull(cnt)) {
            return (Integer) cnt;
        }
        return 0;
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return Integer
     */
    default Integer updateFieldByCondition(Dict data, Table<String, String, Object> condition) {
        Object cnt = callMethod("updateFieldByCondition", data, condition);
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
    default boolean checkRecordIsExists(Table<String, String, Object> condition) {
        Object exists = callMethod("checkRecordIsExists", condition);
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
    default <T> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions, Dict extraData) {
        return GXCommonUtils.convertSourceToTarget(reqDto, targetClass, methodName, copyOptions, extraData);
    }

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @param methodName  转换方法名字
     * @param copyOptions 转换的自定义项
     * @return
     */
    default <T> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions) {
        return sourceToTarget(reqDto, targetClass, methodName, copyOptions, Dict.create());
    }

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @return
     */
    default <T> T sourceToTarget(Q reqDto, Class<T> targetClass) {
        return sourceToTarget(reqDto, targetClass, null, null);
    }

    /**
     * 设置服务类的Class对象
     *
     * @param serveServiceClass 服务类Class对象
     */
    default void staticBindServeServiceClass(Class<?> serveServiceClass) {
        this.serveServiceClassMap.put(getClass().getSimpleName(), serveServiceClass);
    }

    /**
     * 子类可以动态指定目标服务类型
     *
     * @return
     */
    default GXBaseServeApi callBindTargetServeSericeClass(Class<?> targetServeServiceClass) {
        targetServeServiceClassThreadLocal.set(targetServeServiceClass);
        return this;
    }

    /**
     * 调用指定类中的指定方法
     *
     * @param methodName 方法名字
     * @param params     参数列表
     * @return Object
     */
    default Object callMethod(String methodName, Object... params) {
        Class<?> serveServiceClass = targetServeServiceClassThreadLocal.get();
        if (Objects.nonNull(serveServiceClass)) {
            targetServeServiceClassThreadLocal.remove();
        }
        if (Objects.isNull(serveServiceClass)) {
            serveServiceClass = serveServiceClassMap.get(getClass().getSimpleName());
        }
        if (Objects.nonNull(serveServiceClass)) {
            return GXCommonUtils.reflectCallObjectMethod(serveServiceClass, methodName, params);
        }
        return null;
    }
}
