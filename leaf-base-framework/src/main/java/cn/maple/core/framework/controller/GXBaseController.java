package cn.maple.core.framework.controller;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.dto.protocol.res.GXBaseResProtocol;
import cn.maple.core.framework.dto.protocol.res.GXPaginationResProtocol;
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
    default <S, Q> Q convertSourceToTarget(S source, Class<Q> clazz) {
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
    default <S, Q> Q convertSourceToTarget(S source, Class<Q> clazz, String methodName) {
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
    default <S, Q> Q convertSourceToTarget(S source, Class<Q> clazz, CopyOptions copyOptions) {
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
    default <S, Q> Q convertSourceToTarget(S source, Class<Q> clazz, String methodName, CopyOptions copyOptions) {
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
    default <S, Q> List<Q> convertSourceListToTargetList(Collection<S> collection, Class<Q> clazz, String methodName, CopyOptions copyOptions) {
        return GXCommonUtils.convertSourceListToTargetList(collection, clazz, methodName, copyOptions);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param collection 需要转换的对象列表
     * @param clazz      目标对象的类型
     * @return List
     */
    default <S, Q> List<Q> convertSourceListToTargetList(Collection<S> collection, Class<Q> clazz) {
        return GXCommonUtils.convertSourceListToTargetList(collection, clazz, null, null);
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
     * @param sourceClazz 原类型
     * @param targetClazz 目标类型
     * @param mapStruct   转换器
     * @return GXPaginationResProtocol对象
     */
    default <S extends GXBaseResDto, T extends GXBaseResProtocol>
    GXPaginationResProtocol<T> convertPaginationResToProtocol(GXPaginationResDto<S> pagination,
                                                              GXBaseMapStruct<S, T> mapStruct,
                                                              Class<S> sourceClazz,
                                                              Class<T> targetClazz) {
        List<S> records = pagination.getRecords();
        long total = pagination.getTotal();
        long pages = pagination.getPages();
        long pageSize = pagination.getPageSize();
        long currentPage = pagination.getCurrentPage();
        List<T> list = mapStruct.sourceToTarget(records);
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
}
