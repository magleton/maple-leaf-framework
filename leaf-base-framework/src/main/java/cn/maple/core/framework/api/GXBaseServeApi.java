package cn.maple.core.framework.api;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.protocol.req.GXQueryParamReqProtocol;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseApiResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition);

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件 中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param orderField 排序字段
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition, Map<String, String> orderField);

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件 中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition, Object extraData);

    /**
     * 根据条件获取数据
     *
     * @param condition   查询条件 中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param columns     需要查询的列
     * @param targetClazz 目标类型
     * @return List
     */
    <E> List<E> findFieldByCondition(Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz);

    /**
     * 根据条件获取一条数据
     *
     * @param condition 查询条件 中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @return R
     */
    R findOneByCondition(Table<String, String, Object> condition);

    /**
     * 根据条件获取一条数据
     *
     * @param condition 查询条件  中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @return R
     */
    R findOneByCondition(Table<String, String, Object> condition, Object extraData);

    /**
     * 创建或者更新数据
     *
     * @param reqDto      请求参数
     * @param condition   更新条件  中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param copyOptions 复制可选项
     * @return ID
     */
    ID updateOrCreate(Q reqDto, Table<String, String, Object> condition, CopyOptions copyOptions);

    /**
     * 创建或者更新数据
     *
     * @param reqDto      请求参数
     * @param copyOptions 复制可选项
     * @return ID
     */
    ID updateOrCreate(Q reqDto, CopyOptions copyOptions);

    /**
     * 创建或者更新数据
     *
     * @param reqDto 请求参数
     * @return ID
     */
    ID updateOrCreate(Q reqDto);

    /**
     * 分页数据
     *
     * @param reqProtocol 查询条件
     * @param copyOptions 复制可选项
     * @return 分页对象
     */
    GXPaginationResDto<R> paginate(GXQueryParamReqProtocol reqProtocol, CopyOptions copyOptions);

    /**
     * 分页数据
     *
     * @param reqProtocol 查询条件
     * @return 分页对象
     */
    GXPaginationResDto<R> paginate(GXQueryParamReqProtocol reqProtocol);

    /**
     * 物理删除
     *
     * @param condition 删除条件
     * @return 删除行数
     */
    Integer deleteCondition(Table<String, String, Object> condition);

    /**
     * 软删除
     *
     * @param condition 删除条件
     * @return 删除行数
     */
    Integer deleteSoftCondition(Table<String, String, Object> condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param updateFields 需要更新的数据
     * @param condition    更新条件
     * @return Integer
     */
    Integer updateFieldByCondition(List<GXUpdateField<?>> updateFields, Table<String, String, Object> condition);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(Table<String, String, Object> condition);

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
    <T> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions, Dict extraData);

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @param methodName  转换方法名字
     * @param copyOptions 转换的自定义项
     * @return
     */
    <T> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions);

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @return
     */
    <T> T sourceToTarget(Q reqDto, Class<T> targetClass);

    /**
     * 设置服务类的Class对象
     * 在子类的构造函数中调用
     *
     * @param serveServiceClass 服务类Class对象
     */
    void staticBindServeServiceClass(Class<?> serveServiceClass);

    /**
     * 子类可以动态指定目标服务类型
     * 方法调用的时候自己调用
     *
     * @return GXBaseServeApi
     */
    GXBaseServeApi<Q, R, ID> callBindTargetServeSericeClass(Class<?> targetServeServiceClass);

    /**
     * 调用指定类中的指定方法
     *
     * @param methodName 方法名字
     * @param params     参数列表
     * @return Object
     */
    Object callMethod(String methodName, Object... params);

    /**
     * 获取底层服务类的Class
     *
     * @return
     */
    Class<?> getServeServiceClass();

    /**
     * 获取返回的Class
     *
     * @return
     */
    Class<R> getGenericClassType();

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param extraData 额外数据
     * @return List
     */
    List<GXCondition<?>> convertTableConditionToConditionExp(Table<String, String, Object> condition);

    /**
     * 将Table类型的条件转换为条件表达式
     *
     * @param tableNameAlias 表别名
     * @param condition      原始条件
     * @return 转换后的条件
     */
    List<GXCondition<?>> convertTableConditionToConditionExp(String tableNameAlias, Table<String, String, Object> condition);

    /**
     * 获取表的名字
     *
     * @return
     */
    String getTableName();
}
