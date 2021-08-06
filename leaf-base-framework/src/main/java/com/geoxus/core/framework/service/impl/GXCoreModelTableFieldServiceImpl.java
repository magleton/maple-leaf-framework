package com.geoxus.core.framework.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.framework.entity.GXCoreModelTableFieldEntity;
import com.geoxus.core.framework.mapper.GXCoreModelTableFieldMapper;
import com.geoxus.core.framework.service.GXCoreModelTableFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@GXDataSourceAnnotation("framework")
public class GXCoreModelTableFieldServiceImpl extends ServiceImpl<GXCoreModelTableFieldMapper, GXCoreModelTableFieldEntity> implements GXCoreModelTableFieldService {
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
        return getOne(queryWrapper);
    }

    /**
     * 通过条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    @Override
    public List<Dict> getModelAttributesByModelId(Dict condition) {
        return baseMapper.getModelAttributesByModelId(condition);
    }
}
