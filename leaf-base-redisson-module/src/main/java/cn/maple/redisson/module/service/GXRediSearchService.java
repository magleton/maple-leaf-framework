package cn.maple.redisson.module.service;

import cn.hutool.core.lang.Dict;
import cn.maple.redisson.module.dto.GXBaseRediSeachParamInnerDto;

import java.util.List;

@SuppressWarnings("all")
public interface GXRediSearchService {
    /**
     * 按照条件进行搜索
     *
     * @param paramInnerDto 搜索条件
     * @return List
     */
    List<Dict> search(GXBaseRediSeachParamInnerDto paramInnerDto);
}
