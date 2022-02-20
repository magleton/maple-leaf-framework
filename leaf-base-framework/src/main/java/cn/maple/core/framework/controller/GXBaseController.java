package cn.maple.core.framework.controller;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.dto.GXBaseData;
import cn.maple.core.framework.dto.protocol.res.GXBaseResProtocol;
import cn.maple.core.framework.dto.protocol.res.GXPaginationResProtocol;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface GXBaseController {
    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @param clazz  目标对象类型
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz) {
        return convertSourceToTarget(source, clazz, null, null);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source     源对象
     * @param clazz      目标对象类型
     * @param methodName 回调的方法名字
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz, String methodName) {
        return convertSourceToTarget(source, clazz, methodName, null);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source      源对象
     * @param clazz       目标对象类型
     * @param copyOptions 复制选项
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz, CopyOptions copyOptions) {
        return convertSourceToTarget(source, clazz, null, copyOptions);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source      源对象
     * @param clazz       目标对象类型
     * @param methodName  回调的方法名字
     * @param copyOptions 复制选项
     * @return T
     */
    default <S, T> T convertSourceToTarget(S source, Class<T> clazz, String methodName, CopyOptions copyOptions) {
        return GXCommonUtils.convertSourceToTarget(source, clazz, methodName, copyOptions);
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
        return GXCommonUtils.convertSourceListToTargetList(collection, clazz, methodName, copyOptions);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param collection 需要转换的对象列表
     * @param clazz      目标对象的类型
     * @return List
     */
    default <S, T> List<T> convertSourceListToTargetList(Collection<S> collection, Class<T> clazz) {
        return convertSourceListToTargetList(collection, clazz, null, null);
    }

    /**
     * 将任意对象列表通过转换器转换为指定类型的目标对象列表
     *
     * @param targetList 目标列表
     * @param mapStruct  转换器
     * @return 源对象列表
     */
    default <S extends GXBaseData, T extends GXBaseData> List<S> convertTargetListToSourceList(List<T> targetList, GXBaseMapStruct<S, T> mapStruct) {
        return GXCommonUtils.convertTargetListToSourceList(targetList, mapStruct);
    }

    /**
     * 将任意目标对象通过转换器转换为指定类型的目标对象
     *
     * @param target    目标对象
     * @param mapStruct 转换器
     * @return 源对象
     */
    default <S extends GXBaseData, T extends GXBaseData> S convertTargetToSource(T target, GXBaseMapStruct<S, T> mapStruct) {
        return GXCommonUtils.convertTargetToSource(target, mapStruct);
    }

    /**
     * 将任意的源对象列表通过转换器转换为指定类型的目标对象列表
     *
     * @param sourceList 源列表
     * @param mapStruct  转换器
     * @return 目标对象列表
     */
    default <S extends GXBaseData, T extends GXBaseData> List<T> convertSourceListToTargetList(List<S> sourceList, GXBaseMapStruct<S, T> mapStruct) {
        return GXCommonUtils.convertSourceListToTargetList(sourceList, mapStruct);
    }

    /**
     * 将任意源对象通过转换器转换为目标对象
     *
     * @param source    源对象
     * @param mapStruct 转换器
     * @return 目标对象
     */
    default <S extends GXBaseData, T extends GXBaseData> T convertSourceToTarget(S source, GXBaseMapStruct<S, T> mapStruct) {
        return GXCommonUtils.convertSourceToTarget(source, mapStruct);
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
            throw new GXBusinessException(CharSequenceUtil.format("token中不存在键为{}的值", tokenFieldName));
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
        List<S> records = pagination.getRecords();
        long total = pagination.getTotal();
        long pages = pagination.getPages();
        long pageSize = pagination.getPageSize();
        long currentPage = pagination.getCurrentPage();
        List<T> list = convertSourceListToTargetList(records, targetClazz);
        return new GXPaginationResProtocol<>(list, total, pages, pageSize, currentPage);
    }

    /**
     * 构建菜单树
     *
     * @param sourceList      源列表
     * @param rootParentValue 根父级的值, 一般是 0
     * @param <R>             返回的数据类型
     * @return 列表
     */
    default <R extends GXBaseResProtocol> List<R> buildTree(List<R> sourceList, Object rootParentValue) {
        return GXCommonUtils.buildDeptTree(sourceList, rootParentValue);
    }

    /**
     * 获取前端用户的登录ID
     *
     * @param tokenSecretKey token密钥
     * @param targetClass    返回的数据类型
     * @return R
     */
    default <R> R getFrontEndUserId(String tokenSecretKey, Class<R> targetClass) {
        if (Objects.isNull(tokenSecretKey)) {
            throw new GXBusinessException("请传递token密钥!");
        }
        return getLoginFieldFromToken(GXTokenConstant.TOKEN_NAME, GXTokenConstant.USER_ID, targetClass, tokenSecretKey);
    }

    /**
     * 获取后端用户的登录ID(管理端)
     *
     * @param tokenSecretKey token密钥
     * @param targetClass    返回的数据类型
     * @return R
     */
    default <R> R getBackEndUserId(String tokenSecretKey, Class<R> targetClass) {
        if (Objects.isNull(tokenSecretKey)) {
            throw new GXBusinessException("请传递token密钥!");
        }
        return getLoginFieldFromToken(GXTokenConstant.TOKEN_NAME, GXTokenConstant.ADMIN_ID, targetClass, tokenSecretKey);
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
}
