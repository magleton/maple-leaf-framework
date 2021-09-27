package com.geoxus.core.framework.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.core.datasource.annotation.GXDataSource;
import com.geoxus.core.framework.dao.GXCoreAttributesDao;
import com.geoxus.core.framework.entity.GXCoreAttributesEntity;
import com.geoxus.core.framework.mapper.GXCoreAttributesMapper;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@GXDataSource("framework")
public class GXCoreAttributesServiceImpl extends GXBusinessServiceImpl<GXCoreAttributesEntity, GXCoreAttributesMapper, GXCoreAttributesDao> implements GXCoreAttributesService {
    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #p0")
    public List<GXCoreAttributesEntity> getAttributesByCategory(String category) {
        return Optional.ofNullable(baseDao.list(new QueryWrapper<GXCoreAttributesEntity>().eq("category", category))).orElse(new ArrayList<>());
    }

    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #attributeName")
    public GXCoreAttributesEntity getAttributeByAttributeName(String attributeName) {
        attributeName = CharSequenceUtil.toUnderlineCase(attributeName);
        return baseDao.getOne(new QueryWrapper<GXCoreAttributesEntity>().eq("attribute_name", attributeName));
    }

    @Override
    public boolean checkFieldIsExists(String attributeName) {
        attributeName = CharSequenceUtil.toUnderlineCase(attributeName);
        Dict condition = Dict.create().set("attribute_name", attributeName);
        return 1 == checkRecordIsExists(GXCoreAttributesEntity.class, condition);
    }

    @Override
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        Dict condition = Dict.create().set(field, value);
        return 1 == checkRecordIsExists(GXCoreAttributesEntity.class, condition);
    }

    @Override
    public <R> GXPaginationProtocol<R> listOrSearchPage(Dict param) {
        final IPage<R> riPage = constructPageObjectFromParam(param);
        final List<R> list = getBaseMapper().listOrSearchPage(riPage, param);
        riPage.setRecords(list);
        return new GXPaginationProtocol<>(riPage.getRecords(), riPage.getTotal(), riPage.getSize(), riPage.getCurrent());
    }

    public long create(GXCoreAttributesEntity target, Dict param) {
        boolean save = baseDao.save(target);
        if (save) {
            return target.getAttributeId();
        }
        return 0;
    }

    public long update(GXCoreAttributesEntity target, Dict param) {
        String newExt = target.getExt();
        if (null != newExt && JSONUtil.isJson(newExt)) {
            String oldExt = getSingleFieldValueByDB(GXCoreAttributesEntity.class, "ext", String.class, Dict.create().set("attribute_id", target.getAttributeId()));
            if (JSONUtil.isJson(oldExt)) {
                Dict oldExtData = JSONUtil.toBean(oldExt, Dict.class);
                Dict newExtData = JSONUtil.toBean(newExt, Dict.class);
                oldExtData.putAll(newExtData);
                target.setExt(JSONUtil.toJsonStr(oldExtData));
            }
        }
        boolean update = baseDao.updateById(target);
        if (update) {
            return target.getAttributeId();
        }
        return 0;
    }

    @Override
    public Dict detail(Dict param) {
        return getBaseMapper().detail(param);
    }
}
