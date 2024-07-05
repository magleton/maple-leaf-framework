package cn.maple.core.framework.controller;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.dto.protocol.res.GXBaseResProtocol;
import cn.maple.core.framework.dto.protocol.res.GXPaginationResProtocol;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface GXBaseController {
    /**
     * 日志对象
     */
    Logger LOG = LoggerFactory.getLogger(GXBaseController.class);

    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @param clazz  目标对象类型
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz) {
        return convertSourceToTarget(source, clazz, null, null, Dict.create());
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source     源对象
     * @param clazz      目标对象类型
     * @param methodName 方法名字
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz, String methodName) {
        return convertSourceToTarget(source, clazz, methodName, null, Dict.create());
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source    源对象
     * @param clazz     目标对象类型
     * @param extraData 额外数据
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz, Dict extraData) {
        return convertSourceToTarget(source, clazz, null, null, extraData);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source     源对象
     * @param clazz      目标对象类型
     * @param methodName 回调的方法名字
     * @param extraData  额外数据
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz, String methodName, Dict extraData) {
        return convertSourceToTarget(source, clazz, methodName, null, extraData);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source      源对象
     * @param clazz       目标对象类型
     * @param copyOptions 复制选项
     * @param extraData   额外数据
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz, CopyOptions copyOptions, Dict extraData) {
        return convertSourceToTarget(source, clazz, null, copyOptions, extraData);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source      源对象
     * @param clazz       目标对象类型
     * @param methodName  回调的方法名字
     * @param copyOptions 复制选项
     * @param extraData   额外数据
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz, String methodName, CopyOptions copyOptions, Dict extraData) {
        if (Objects.isNull(methodName)) {
            methodName = GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME;
        }
        return GXCommonUtils.convertSourceToTarget(source, clazz, methodName, copyOptions, extraData);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param collection  需要转换的对象列表
     * @param clazz       目标对象的类型
     * @param methodName  回调方法的名字
     * @param copyOptions 需要拷贝的选项
     * @param extraData   额外数据
     * @return List
     */
    default <S, T> List<T> convertSourceListToTargetList(Collection<S> collection, Class<T> clazz, String methodName, CopyOptions copyOptions, Dict extraData) {
        if (Objects.isNull(methodName)) {
            methodName = GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME;
        }
        return GXCommonUtils.convertSourceListToTargetList(collection, clazz, methodName, copyOptions, extraData);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param collection  需要转换的对象列表
     * @param clazz       目标对象的类型
     * @param methodName  回调方法的名字
     * @param copyOptions 需要拷贝的选项
     * @return List
     */
    default <S, T> List<T> convertSourceListToTargetList(Collection<S> collection, Class<T> clazz, String methodName, CopyOptions copyOptions) {
        return convertSourceListToTargetList(collection, clazz, methodName, copyOptions, Dict.create());
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param collection 需要转换的对象列表
     * @param clazz      目标对象的类型
     * @return List
     */
    default <S, T> List<T> convertSourceListToTargetList(Collection<S> collection, Class<T> clazz) {
        return convertSourceListToTargetList(collection, clazz, null, null, Dict.create());
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param collection 需要转换的对象列表
     * @param clazz      目标对象的类型
     * @param extraData  额外数据
     * @return List
     */
    default <S, T> List<T> convertSourceListToTargetList(Collection<S> collection, Class<T> clazz, Dict extraData) {
        return convertSourceListToTargetList(collection, clazz, null, null, extraData);
    }

    /**
     * 从token中获取登录用户ID
     *
     * @param tokenName      header中Token的名字 eg : Authorization、token、adminToken
     * @param tokenFieldName Token中包含的ID名字 eg : id、userId、adminId....
     * @param clazz          返回值类型
     * @param secretKey      加解密KEY
     * @return R
     */
    default <R> R getLoginFieldFromToken(String tokenName, String tokenFieldName, Class<R> clazz, String secretKey) {
        R fieldFromToken = GXCurrentRequestContextUtils.getLoginFieldFromToken(tokenName, tokenFieldName, clazz, secretKey);
        if (Objects.isNull(fieldFromToken)) {
            LOG.error("token中不存在键为{}的值", tokenFieldName);
        }
        return fieldFromToken;
    }

    /**
     * 将GXPaginationResDto转换为GXPaginationResProtocol
     *
     * @param pagination  源分页对象
     * @param targetClazz 目标类型
     * @return GXPaginationResProtocol对象
     */
    default <S extends GXBaseResDto, T extends GXBaseResProtocol> GXPaginationResProtocol<T> convertPaginationResToProtocol(GXPaginationResDto<S> pagination, Class<T> targetClazz) {
        return convertPaginationResToProtocol(pagination, targetClazz, null);
    }

    /**
     * 将GXPaginationResDto转换为GXPaginationResProtocol
     *
     * @param pagination  源分页对象
     * @param targetClazz 目标类型
     * @param copyOptions 转换器对象
     * @return GXPaginationResProtocol对象
     */
    default <S extends GXBaseResDto, T extends GXBaseResProtocol> GXPaginationResProtocol<T> convertPaginationResToProtocol(GXPaginationResDto<S> pagination, Class<T> targetClazz, CopyOptions copyOptions) {
        List<S> records = pagination.getRecords();
        long total = pagination.getTotal();
        long pages = pagination.getPages();
        long pageSize = pagination.getPageSize();
        long currentPage = pagination.getCurrentPage();
        List<T> list = convertSourceListToTargetList(records, targetClazz, null, copyOptions);
        return new GXPaginationResProtocol<>(list, total, pages, pageSize, currentPage);
    }

    /**
     * 构建菜单树
     *
     * @param sourceList          源列表
     * @param rootParentValue     根父级的值, 一般是 0
     * @param getParentMethodName 父级字段的get方法名字
     * @param <R>                 返回的数据类型
     * @return 列表
     */
    default <R extends GXBaseResProtocol> List<R> buildTree(List<R> sourceList, Object rootParentValue, String getParentMethodName) {
        return GXCommonUtils.buildTree(sourceList, rootParentValue, getParentMethodName);
    }

    /**
     * 获取前端用户的登录ID
     *
     * @param tokenName      token名字
     * @param tokenSecretKey token密钥
     * @param targetClass    返回的数据类型
     * @return R
     */
    default <R> R getFrontEndUserId(String tokenName, String tokenSecretKey, Class<R> targetClass) {
        if (Objects.isNull(tokenSecretKey)) {
            throw new GXBusinessException("请传递token密钥!");
        }
        return getLoginFieldFromToken(tokenName, GXTokenConstant.TOKEN_USER_ID_FIELD_NAME, targetClass, tokenSecretKey);
    }

    /**
     * 获取前端用户的登录ID
     *
     * @param tokenSecretKey token密钥
     * @param targetClass    返回的数据类型
     * @return R
     */
    default <R> R getFrontEndUserId(String tokenSecretKey, Class<R> targetClass) {
        return getFrontEndUserId(GXTokenConstant.TOKEN_NAME, tokenSecretKey, targetClass);
    }

    /**
     * 获取后端用户的登录ID(管理端)
     *
     * @param tokenName      token名字
     * @param tokenSecretKey token密钥
     * @param targetClass    返回的数据类型
     * @return R
     */
    default <R> R getManagerUserId(String tokenName, String tokenSecretKey, Class<R> targetClass) {
        if (Objects.isNull(tokenSecretKey)) {
            throw new GXBusinessException("请传递token密钥!");
        }
        return getLoginFieldFromToken(tokenName, GXTokenConstant.TOKEN_USER_ID_FIELD_NAME, targetClass, tokenSecretKey);
    }

    /**
     * 获取后端用户的登录ID(管理端)
     *
     * @param tokenSecretKey token密钥
     * @param targetClass    返回的数据类型
     * @return R
     */
    default <R> R getManagerUserId(String tokenSecretKey, Class<R> targetClass) {
        return getManagerUserId(GXTokenConstant.TOKEN_NAME, tokenSecretKey, targetClass);
    }

    /**
     * 从token中获取登录信息
     *
     * @param tokenName      token名字
     * @param tokenSecretKey token密钥
     * @return Dict
     */
    default Dict getLoginCredentials(String tokenName, String tokenSecretKey) {
        return GXCurrentRequestContextUtils.getLoginCredentials(tokenName, tokenSecretKey);
    }

    /**
     * 拼接字段的断言信息
     *
     * @param msg       提示信息
     * @param fieldName 字段名字
     * @return 断言信息
     */
    default String concatAssertMsg(String msg, String fieldName) {
        return CharSequenceUtil.format("{}{}{}", fieldName, StrPool.COLON, msg);
    }
}
