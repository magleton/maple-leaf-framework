package cn.maple.redisson.module.service;

import cn.maple.core.framework.dto.inner.GXBaseSEParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;

import java.util.List;

@SuppressWarnings("all")
public interface GXRediSearchService<R extends GXBaseResDto> {
    /**
     * 按照条件进行搜索
     *
     * @param paramInnerDto 搜索条件
     * @return List
     */
    List<R> search(GXBaseSEParamInnerDto paramInnerDto);
}
