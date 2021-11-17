package cn.maple.core.framework.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.maple.core.framework.dto.inner.req.GXBaseReqDto;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.dto.protocol.req.GXBaseReqProtocol;
import cn.maple.core.framework.dto.protocol.res.GXBaseResProtocol;
import cn.maple.core.framework.dto.protocol.res.GXPaginationResProtocol;
import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.core.framework.util.GXHttpContextUtils;

import java.util.List;

public interface GXBaseController {
    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @param clazz  目标对象类型
     * @return T
     */
    default <S extends GXBaseReqProtocol, Q extends GXBaseReqDto> Q convertSourceToTarget(S source, Class<Q> clazz) {
        return BeanUtil.copyProperties(source, clazz);
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
        return GXHttpContextUtils.getLoginIdFromToken(tokenName, tokenIdName, clazz, secretKey);
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
    default <S extends GXBaseResDto, T extends GXBaseResProtocol> GXPaginationResProtocol<T> convertPaginationResToProtocol(GXPaginationResDto<S> pagination,
                                                                                                                            Class<S> sourceClazz,
                                                                                                                            Class<T> targetClazz,
                                                                                                                            GXBaseMapStruct<S, T> mapStruct) {
        List<S> records = pagination.getRecords();
        long total = pagination.getTotal();
        long pages = pagination.getPages();
        long pageSize = pagination.getPageSize();
        long currentPage = pagination.getCurrentPage();
        List<T> list = mapStruct.sourceToTarget(records);
        return new GXPaginationResProtocol<>(list, total, pages, pageSize, currentPage);
    }
}
