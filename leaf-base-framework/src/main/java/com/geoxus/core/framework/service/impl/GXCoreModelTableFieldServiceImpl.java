package com.geoxus.core.framework.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geoxus.core.datasource.annotation.GXDataSource;
import com.geoxus.core.framework.dao.GXCoreModelTableFieldDao;
import com.geoxus.core.framework.entity.GXCoreModelTableFieldEntity;
import com.geoxus.core.framework.mapper.GXCoreModelTableFieldMapper;
import com.geoxus.core.framework.service.GXCoreModelTableFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@GXDataSource("framework")
public class GXCoreModelTableFieldServiceImpl extends com.geoxus.core.service.impl.GXDBBaseServiceImpl<GXCoreModelTableFieldEntity, GXCoreModelTableFieldMapper, GXCoreModelTableFieldDao> implements GXCoreModelTableFieldService, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService, com.geoxus.core.service.GXDBBaseService<GXCoreModelTableFieldEntity, GXCoreModelTableFieldMapper, GXCoreModelTableFieldDao> {
    /**
     * 通过条件获取数据
     *
     * @param condition 分类名字
     * @return List
     */
    @Override
    public GXCoreModelTableFieldEntity getCoreModelDbFieldByCondition(Dict condition) {
        final QueryWrapper<GXCoreModelTableFieldEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(condition);
        return baseDao.getOne(queryWrapper);
    }

    /**
     * 通过条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #p0.getStr('core_model_id') + #p0.getStr('table_field_name')")
    public List<Dict> getModelAttributesByModelId(Dict condition) {
        return getBaseMapper().getModelAttributesByCondition(condition);
    }
}
