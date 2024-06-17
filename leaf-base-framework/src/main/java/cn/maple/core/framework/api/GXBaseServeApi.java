package cn.maple.core.framework.api;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.api.dto.req.GXBaseApiReqDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.protocol.req.GXQueryParamReqProtocol;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 暴露服务的基础API接口
 */
@SuppressWarnings("all")
public interface GXBaseServeApi {
    /**
     * 根据条件获取数据
     *
     * @param condition   查询条件
     * @param targetClazz 返回的数据类型
     * @return List
     */
    <R extends GXBaseResDto> List<R> findByCondition(Table<String, String, Object> condition, Class<R> targetClazz);

    /**
     * 通过条件查询列表信息
     *
     * @param condition   搜索条件 中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param orderField  排序字段
     * @param targetClazz 返回数据类型
     * @return List
     */
    <R extends GXBaseResDto> List<R> findByCondition(Table<String, String, Object> condition, Map<String, String> orderField, Class<R> targetClazz);

    /**
     * 根据条件获取数据
     *
     * @param condition   查询条件 中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param targetClazz 返回数据类型
     * @param extraData   额外的参数 用于数据转换时数据自动填充
     * @return List
     */
    <R extends GXBaseResDto> List<R> findByCondition(Table<String, String, Object> condition, Class<R> targetClazz, Object extraData);

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
     * @param condition   查询条件 中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param targetClazz 数据返回类型
     * @return R
     */
    <R extends GXBaseResDto> R findOneByCondition(Table<String, String, Object> condition, Class<R> targetClazz);

    /**
     * 根据条件获取一条数据
     *
     * @param condition   查询条件  中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param targetClazz 数据返回类型
     * @param extraData   数据转换时 自动填充的数据
     * @return R
     */
    <R extends GXBaseResDto> R findOneByCondition(Table<String, String, Object> condition, Class<R> targetClazz, Object extraData);

    /**
     * 根据条件获取一条数据
     *
     * @param condition   查询条件  中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param columns     待查询的列
     * @param targetClazz 数据返回类型
     * @param extraData   数据转换时 自动填充的数据
     * @return R
     */
    <R extends GXBaseResDto> R findOneByCondition(Table<String, String, Object> condition, Set<String> columns, Class<R> targetClazz, Object extraData);

    /**
     * 通过ID查询一条数据
     *
     * @param id          待查询的ID
     * @param columns     需要查询的列
     * @param targetClazz 返回的数据剋新
     * @return R
     */
    <R extends GXBaseResDto> R findById(Long id, Set<String> columns, Class<R> targetClazz);

    /**
     * 通过ID查询一条数据
     *
     * @param id          待查询的ID
     * @param targetClazz 返回的数据剋新
     * @return R
     */
    <R extends GXBaseResDto> R findById(Long id, Class<R> targetClazz);

    /**
     * 获取一条记录的指定单字段
     *
     * @param condition   条件
     * @param column      字段名字
     * @param targetClazz 返回的类型
     * @return 指定的类型
     */
    <E> E findSingleFieldByCondition(Table<String, String, Object> condition, String column, Class<E> targetClazz);

    /**
     * 创建或者更新数据
     *
     * @param reqDto      请求参数
     * @param condition   更新条件  中间表达式请使用 GXBuilderConstant常量中提供的表达式
     * @param copyOptions 复制可选项
     * @return ID
     */
    <ID, Q extends GXBaseApiReqDto> ID updateOrCreate(Q reqDto, Table<String, String, Object> condition, CopyOptions copyOptions);

    /**
     * 创建或者更新数据
     *
     * @param reqDto      请求参数
     * @param copyOptions 复制可选项
     * @return ID
     */
    <ID, Q extends GXBaseApiReqDto> ID updateOrCreate(Q reqDto, CopyOptions copyOptions);

    /**
     * 创建或者更新数据
     *
     * @param reqDto 请求参数
     * @return ID
     */
    <ID, Q extends GXBaseApiReqDto> ID updateOrCreate(Q reqDto);

    /**
     * 分页数据
     *
     * @param reqProtocol 查询条件
     * @param targetClazz 返回的数据类型
     * @param copyOptions 复制可选项
     * @return 分页对象
     */
    <R> GXPaginationResDto<R> paginate(GXQueryParamReqProtocol reqProtocol, Class<R> targetClazz, CopyOptions copyOptions);

    /**
     * 分页数据
     *
     * @param reqProtocol 查询条件
     * @param targetClazz 返回的数据类型
     * @return 分页对象
     */
    <R> GXPaginationResDto<R> paginate(GXQueryParamReqProtocol reqProtocol, Class<R> targetClazz);

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
     * 统计给定条件的记录条数
     *
     * @param condition 条件
     * @return int
     */
    Long count(Table<String, String, Object> condition);

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
    <T, Q extends GXBaseApiReqDto> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions, Dict extraData);

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @param methodName  转换方法名字
     * @param copyOptions 转换的自定义项
     * @return
     */
    <T, Q extends GXBaseApiReqDto> T sourceToTarget(Q reqDto, Class<T> targetClass, String methodName, CopyOptions copyOptions);

    /**
     * 转指定的对象到指定的目标类型对象
     *
     * @param reqDto      请求参数
     * @param targetClass 目标对象类型
     * @return
     */
    <T, Q extends GXBaseApiReqDto> T sourceToTarget(Q reqDto, Class<T> targetClass);

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
    GXBaseServeApi callBindTargetServeSericeClass(Class<?> targetServeServiceClass);

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
}
