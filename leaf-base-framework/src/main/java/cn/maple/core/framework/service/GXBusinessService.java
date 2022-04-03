package cn.maple.core.framework.service;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.maple.core.framework.dto.GXBaseData;
import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.core.framework.util.GXCommonUtils;

import java.util.Collection;
import java.util.List;

public interface GXBusinessService {
    /**
     * 将任意对象列表通过转换器转换为指定类型的目标对象列表
     *
     * @param targetList     目标列表
     * @param mapStructClass 转换器
     * @return 源对象列表
     */
    @SuppressWarnings("all")
    <S extends GXBaseData, T extends GXBaseData, M extends GXBaseMapStruct<S, T>> List<S> convertTargetListToSourceList(List<T> targetList, Class<M> mapStructClass);

    /**
     * 将任意目标对象通过转换器转换为指定类型的目标对象
     *
     * @param target         目标对象
     * @param mapStructClass 转换器
     * @return 目标对象
     */
    <S extends GXBaseData, T extends GXBaseData, M extends GXBaseMapStruct<S, T>> S convertTargetToSource(T target, Class<M> mapStructClass);

    /**
     * 将任意的源对象列表通过转换器转换为指定类型的目标对象列表
     *
     * @param sourceList     源列表
     * @param mapStructClass 转换器
     * @return 目标对象列表
     */
    @SuppressWarnings("all")
    <S extends GXBaseData, T extends GXBaseData, M extends GXBaseMapStruct<S, T>> List<T> convertSourceListToTargetList(List<S> sourceList, Class<M> mapStructClass);

    /**
     * 将任意源对象通过转换器转换为目标对象
     *
     * @param source         源对象
     * @param mapStructClass 转换器
     * @return 目标对象
     */
    <S extends GXBaseData, T extends GXBaseData, M extends GXBaseMapStruct<S, T>> T convertSourceToTarget(S source, Class<M> mapStructClass);

    /**
     * 加密手机号码
     *
     * @param phoneNumber 明文手机号
     * @param key         加密key
     * @return String
     */
    String encryptedPhoneNumber(String phoneNumber, String key);

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @param key                解密key
     * @return String
     */
    String decryptedPhoneNumber(String encryptPhoneNumber, String key);

    /**
     * 隐藏手机号码的指定几位为指定的字符
     *
     * @param phoneNumber  手机号码
     * @param startInclude 开始字符位置(包含,从0开始)
     * @param endExclude   结束字符位置
     * @param replacedChar 替换为的字符
     * @return String
     */
    String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar);

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       )
     *     }
     * </pre>
     *
     * @param entity 实体对象
     * @param path   路径
     * @return R
     */
    <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type);

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       )
     *     }
     * </pre>
     *
     * @param entity       实体对象
     * @param path         路径
     * @param defaultValue 默认值
     * @return R
     */
    <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, R defaultValue);

    /**
     * 将任意对象转换为指定类型的对象
     * <p>
     * {@code}
     * eg:
     * Dict source = Dict.create().set("username","britton").set("realName","枫叶思源");
     * convertSourceToTarget( source , PersonResDto.class, "customerProcess" , null);
     * OR
     * PersonReqProtocol req = new PersonReqProtocol();
     * req.setUsername("britton");
     * req.setRealName("枫叶思源")；
     * convertSourceToTarget(req ,  PersonResDto.class, "customerProcess" , null);
     * {code}
     *
     * @param source       源对象
     * @param tClass       目标对象类型
     * @param methodName   需要条用的方法名字
     * @param copyOptions  复制选项
     * @param customerData 额外数据
     * @return 目标对象
     */
    <S, T> T convertSourceToTarget(S source, Class<T> tClass, String methodName, CopyOptions copyOptions, Object... customerData);

    /**
     * 将任意对象转换为指定类型的对象
     *
     * @param collection   需要转换的对象列表
     * @param tClass       目标对象的类型
     * @param methodName   需要条用的方法名字
     * @param copyOptions  需要拷贝的选项
     * @param customerData 额外数据
     * @return List
     */
    <R> List<R> convertSourceListToTargetList(Collection<?> collection, Class<R> tClass, String methodName, CopyOptions copyOptions, Object... customerData);

    /**
     * 动态调用指定底层类中的方法
     *
     * @param serveClass 底层类型 一定要是Spring Bean容器中的类型
     * @param methodName 方法名字
     * @param params     调用参数
     * @return Object
     */
    default Object callMethod(Class<?> serveClass, String methodName, Object... params) {
        return GXCommonUtils.reflectCallObjectMethod(serveClass, methodName, params);
    }

    /**
     * 动态调用指定底层类中的方法
     *
     * @param targetObject 被调用对象
     * @param methodName   方法名字
     * @param params       调用参数
     * @return Object
     */
    default Object callMethod(Object targetObject, String methodName, Object... params) {
        return GXCommonUtils.reflectCallObjectMethod(targetObject, methodName, params);
    }
}
