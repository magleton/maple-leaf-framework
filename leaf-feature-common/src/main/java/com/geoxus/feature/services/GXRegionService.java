package com.geoxus.feature.services;

import cn.hutool.core.lang.Dict;
import com.geoxus.feature.dao.GXRegionDao;
import com.geoxus.feature.entities.GXRegionEntity;
import com.geoxus.feature.mappers.GXRegionMapper;
import com.geoxus.core.service.GXDBBaseService;

import java.util.List;

public interface GXRegionService extends GXDBBaseService<GXRegionEntity, GXRegionMapper, GXRegionDao> {
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