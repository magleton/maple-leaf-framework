package cn.maple.core.framework.api;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.protocol.req.GXQueryParamReqProtocol;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 暴露服务的基础API接口
 *
 * @param <Q>  请求参数的类型
 * @param <R>  响应对象的类型
 * @param <ID> 唯一标识的类型  一般是一个实体对象的ID类型
 */
@SuppressWarnings("all")
public interface GXBaseServeApi<Q extends GXBaseReqDto, R extends GXBaseResDto, ID extends Serializable> {
    /**
     * 服务类的Class 对象
     */
    Class<?>[] serveServiceClass = new Class<?>[1];

    /**
     * 根据条件获取一条数据
     *
     * @param condition 查询条件
     * @return R
     */
    default R findOneByCondition(Table<String, String, Object> condition) {
        Object r = callMethod("findOneByCondition", condition);
        if (Objects.nonNull(r)) {
            return (R) r;
        }
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    default List<R> findByCondition(Table<String, String, Object> condition) {
        Object rLst = callMethod("findByCondition", condition);
        if (Objects.nonNull(rLst)) {
            return (List<R>) rLst;
        }
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 根据条件获取一条数据
     *
     * @param columns   需要查询的列
     * @param condition 查询条件
     * @return R
     */
    default R findOneByCondition(Set<String> columns, Table<String, String, Object> condition) {
        Object r = callMethod("findOneByCondition", columns, condition);
        if (Objects.nonNull(r)) {
            return (R) r;
        }
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 根据条件获取一条数据
     *
     * @param columns      需要查询的列
     * @param condition    查询条件
     * @param targetClazz  目标类型
     * @param customerData 额外数据
     * @return R
     */
    default <E> E findOneByCondition(Set<String> columns, Table<String, String, Object> condition, Class<E> targetClazz, Object... customerData) {
        R data = findOneByCondition(columns, condition);
        return GXCommonUtils.convertSourceToTarget(data, targetClazz, null, null, customerData);
    }

    /**
     * 根据条件获取数据
     *
     * @param columns   需要查询的列
     * @param condition 查询条件
     * @return List
     */
    default List<R> findByCondition(Set<String> columns, Table<String, String, Object> condition) {
        Object rLst = callMethod("findByCondition", columns, condition);
        if (Objects.nonNull(rLst)) {
            return (List<R>) rLst;
        }
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 根据条件获取数据
     *
     * @param columns      需要查询的列
     * @param condition    查询条件
     * @param customerData 额外数据
     * @return List
     */
    default <E> List<E> findByCondition(Set<String> columns, Table<String, String, Object> condition, Class<E> targetClazz, Object... customerData) {
        List<R> rList = findByCondition(columns, condition);
        return GXCommonUtils.convertSourceListToTargetList(rList, targetClazz, null, null, customerData);
    }

    /**
     * 创建或者更新数据
     *
     * @param reqDto    请求参数
     * @param condition 更新条件
     * @return ID
     */
    default ID updateOrCreate(Q reqDto, Table<String, String, Object> condition) {
        Object cnt = callMethod("updateOrCreate", reqDto, condition);
        if (Objects.nonNull(cnt)) {
            return (ID) cnt;
        }
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 创建或者更新数据
     *
     * @param reqDto 请求参数
     * @return ID
     */
    default ID updateOrCreate(Q reqDto) {
        return updateOrCreate(reqDto, null);
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
            return (GXPaginationResDto<R>) paginate;
        }
        throw new GXBusinessException("请自定义实现!");
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
        throw new GXBusinessException("请自定义实现!");
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
        throw new GXBusinessException("请自定义实现!");
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
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param condition 条件
     * @return int
     */
    default boolean checkRecordIsExists(Table<String, String, Object> condition) {
        Object exists = callMethod("checkRecordIsExists", condition);
        if (Objects.isNull(exists)) {
            return (boolean) exists;
        }
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto       请求参数
     * @param targetClass  目标对象类型
     * @param methodName   转换方法名字
     * @param copyOptions  转换的自定义项
     * @param customerData 额外数据
     * @return
     */
    default <T> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions, Object... customerData) {
        return GXCommonUtils.convertSourceToTarget(reqDto, targetClass, methodName, copyOptions, customerData);
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
    default void bindServeServiceClass(Class<?> serveServiceClass) {
        this.serveServiceClass[0] = serveServiceClass;
    }

    /**
     * 调用指定类中的指定方法
     *
     * @param methodName 方法名字
     * @param params     参数列表
     * @return Object
     */
    default Object callMethod(String methodName, Object... params) {
        if (Objects.nonNull(serveServiceClass[0])) {
            Object bean = GXSpringContextUtils.getBean(serveServiceClass[0]);
            if (Objects.nonNull(bean)) {
                return GXCommonUtils.reflectCallObjectMethod(bean, methodName, params);
            }
        }
        return null;
    }
}
