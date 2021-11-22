package cn.maple.core.framework.controller;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.dto.protocol.res.GXBaseResProtocol;
import cn.maple.core.framework.dto.protocol.res.GXPaginationResProtocol;
import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;

import java.util.Collection;
import java.util.List;

public interface GXBaseController {
    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @param clazz  目标对象类型
     * @return T
     */
    default <S, Q> Q convertSourceToTarget(S source, Class<Q> clazz) {
        return GXCommonUtils.convertSourceToTarget(source, clazz, null);
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param collection  需要转换的对象列表
     * @param clazz       目标对象的类型
     * @param copyOptions 需要拷贝的选项
     * @return List
     */
    default <S, Q> List<Q> convertSourceToTarget(Collection<S> collection, Class<Q> clazz, CopyOptions copyOptions) {
        return GXCommonUtils.convertSourceListToTargetList(collection, clazz, null, copyOptions);
    }

    /**
     * 从token中获取登录用户ID
     *
     * @param tokenName   header中Token的名字 eg : Authorization、token、adminToken
     * @param tokenIdName Token中包含的ID名字 eg : id、userId、adminId....
     * @param clazz       返回值类型
     * @param secretKey   加解密KEY
     * @return R
     */
    default <R> R getLoginIdFromToken(String tokenName, String tokenIdName, Class<R> clazz, String secretKey) {
        return GXCurrentRequestContextUtils.getLoginIdFromToken(tokenName, tokenIdName, clazz, secretKey);
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
}
