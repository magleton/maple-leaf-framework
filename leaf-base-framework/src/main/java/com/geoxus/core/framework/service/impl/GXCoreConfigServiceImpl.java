package com.geoxus.core.framework.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.vo.common.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.framework.constant.GXCoreConfigConstants;
import com.geoxus.core.framework.entity.GXCoreConfigEntity;
import com.geoxus.core.framework.mapper.GXCoreConfigMapper;
import com.geoxus.core.framework.service.GXCoreConfigService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@GXDataSourceAnnotation("framework")
public class GXCoreConfigServiceImpl extends ServiceImpl<GXCoreConfigMapper, GXCoreConfigEntity> implements GXCoreConfigService {
    public long create(GXCoreConfigEntity target, Dict param) {
        save(target);
        return target.getConfigId();
    }

    public long update(GXCoreConfigEntity target, Dict param) {
        updateById(target);
        return target.getConfigId();
    }

    public boolean delete(Dict param) {
        final long id = param.getLong(GXCoreConfigConstants.PRIMARY_KEY);
        final GXCoreConfigEntity entity = getById(id);
        entity.setStatus(GXBusinessStatusCode.DELETED.getCode());
        updateById(entity);
        return false;
    }

    public GXPagination<Dict> listOrSearchPage(Dict param) {
        return new GXPagination<>(Collections.emptyList());
    }

    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    @Override
    public <T> T getConfigObject(String key, Class<T> clazz) {
        QueryWrapper<GXCoreConfigEntity> conditionWrapper = new QueryWrapper<GXCoreConfigEntity>().allEq(Dict.create().set("param_key", key));
        GXCoreConfigEntity entity = getOne(conditionWrapper);
        if (null != entity) {
            String paramValue = entity.getParamValue();
            if (null != paramValue && JSONUtil.isJson(paramValue)) {
                return JSONUtil.toBean(paramValue, clazz);
            }
        }
        return GXCommonUtils.getClassDefaultValue(clazz);
    }

    @Override
    public boolean updateValueByParamKey(String key, String value) {
        Dict condition = Dict.create().set("param_key", key);
        return updateFieldByCondition(GXCoreConfigEntity.class, Dict.create().set("param_value", value), condition);
    }
}
