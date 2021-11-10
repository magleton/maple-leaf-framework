package cn.maple.core.framework.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.maple.core.framework.dto.inner.req.GXBaseReqDto;
import cn.maple.core.framework.dto.protocol.req.GXBaseReqProtocol;
import cn.maple.core.framework.util.GXHttpContextUtils;

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
}
