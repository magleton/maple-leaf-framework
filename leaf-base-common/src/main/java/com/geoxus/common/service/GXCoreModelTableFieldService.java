package com.geoxus.common.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.dao.GXCoreModelTableFieldDao;
import com.geoxus.common.entity.GXCoreModelTableFieldEntity;
import com.geoxus.common.mapper.GXCoreModelTableFieldMapper;

import java.util.List;

public interface GXCoreModelTableFieldService extends com.geoxus.core.service.GXDBBaseService<GXCoreModelTableFieldEntity, GXCoreModelTableFieldMapper, GXCoreModelTableFieldDao>, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService {
    /**
     * 通过条件获取数据
     *
     * @param condition 分类名字
     * @return List
     */
    GXCoreModelTableFieldEntity getCoreModelDbFieldByCondition(Dict condition);

    /**
     * 通过条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    List<Dict> getModelAttributesByModelId(Dict condition);
}
