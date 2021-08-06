package com.geoxus.core.framework.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.service.GXSessionService;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.framework.entity.GXCoreModelAttributesPermissionEntity;
import com.geoxus.core.framework.mapper.GXCoreModelAttributesPermissionMapper;
import com.geoxus.core.framework.service.GXCoreModelAttributePermissionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@GXDataSourceAnnotation("framework")
public class GXCoreModelAttributePermissionServiceImpl extends ServiceImpl<GXCoreModelAttributesPermissionMapper, GXCoreModelAttributesPermissionEntity> implements GXCoreModelAttributePermissionService {
    @Override
    @Cacheable(cacheManager = "caffeineCache", value = "FRAMEWORK-CACHE", key = "targetClass + methodName + #coreModelId")
    public Dict getModelAttributePermissionByCoreModelId(int coreModelId, Dict param) {
        GXSessionService sessionService = GXSpringContextUtils.getBean(GXSessionService.class);
        final List<Dict> attributes = baseMapper.getModelAttributePermissionByModelId(Dict.create().set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId));
        final Dict data = Dict.create();
        final Dict jsonFieldDict = Dict.create();
        final Dict dbFieldDict = Dict.create();
        List<String> roles = CollUtil.newArrayList();
        final List<String> users = CollUtil.newArrayList();
        final Long currentAdminId = Objects.requireNonNull(sessionService).currentSessionUserId(); // GXCommonUtils.getCurrentSessionUserId();
        final Long superAdminId = GXCommonUtils.getEnvironmentValue("super.admin.id", Long.class);
        if (superAdminId > 0 && currentAdminId.equals(superAdminId)) {
            return Dict.create();
        }
        if (currentAdminId > 0) {
            users.add(currentAdminId.toString());
            // TODO 通过currentAdminId获取当前用户的角色列表
            // Objects.requireNonNull(GXSpringContextUtils.getBean(GXSAdminHasRolesService.class)).getAdminRoles(currentAdminId);
            final Dict adminRoles = sessionService.adminRoles(currentAdminId); //Dict.create();
            roles = adminRoles.keySet().stream().map(r -> Convert.toStr(r, "0")).collect(Collectors.toList());
        }
        for (Dict dict : attributes) {
            final String dbFieldName = dict.getStr("db_field_name");
            final String[] tmpRoles = CharSequenceUtil.split(dict.getStr("roles"), ",");
            final String[] tmpUsers = CharSequenceUtil.split(dict.getStr("users"), ",");
            if (!CollUtil.containsAny(Arrays.asList(tmpUsers), users) && !CollUtil.containsAny(Arrays.asList(tmpRoles), roles)) {
                continue;
            }
            if (CharSequenceUtil.contains(dbFieldName, "::")) {
                final String[] strings = CharSequenceUtil.split(dbFieldName, "::");
                final Dict convertDict = Convert.convert(Dict.class, jsonFieldDict.getOrDefault(strings[0], Dict.create()));
                convertDict.set(CharSequenceUtil.format("{}", strings[1]), CharSequenceUtil.format("{}", String.join("::", strings)));
                jsonFieldDict.set(strings[0], convertDict);
            }
            dbFieldDict.set(dbFieldName, dbFieldName);
        }
        return data.set("json_field", jsonFieldDict).set("db_field", dbFieldDict);
    }
}
