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
    default <S extends GXBaseReqProtocol, T extends GXBaseReqDto> T convertSourceToTarget(S source, Class<T> clazz) {
        return BeanUtil.copyProperties(source, clazz);
    }

    /**
     * 从token中获取用户ID
     *
     * @param tokenName   token的名字
     * @param tokenIdName token中的字段表示
     * @return Long
     */
    default long getUserIdFromToken(String tokenName, String tokenIdName) {
        return GXHttpContextUtils.getUserIdFromToken(tokenName, tokenIdName);
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
