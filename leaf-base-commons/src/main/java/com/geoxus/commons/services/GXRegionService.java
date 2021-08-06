package com.geoxus.commons.services;

import cn.hutool.core.lang.Dict;
import com.geoxus.commons.entities.GXRegionEntity;
import com.geoxus.core.framework.service.GXBaseService;

import java.util.List;

public interface GXRegionService extends GXBaseService<GXRegionEntity> {
    /**
     * 获取所有区域树
     *
     * @return
     */
    List<GXRegionEntity> getRegionTree();

    /**
     * 通过条件获取区域
     *
     * @param param
     * @return
     */
    List<GXRegionEntity> getRegion(Dict param);

    /**
     * 转换名字到拼音
     *
     * @return
     */
    boolean convertNameToPinYin();
}