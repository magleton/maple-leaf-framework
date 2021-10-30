package cn.maple.shiro.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.maple.core.datasource.service.impl.GXDBBaseServiceImpl;
import cn.maple.shiro.dao.GXPermissionsDao;
import cn.maple.shiro.dto.res.GXPermissionsResDto;
import cn.maple.shiro.entities.GXPermissionsEntity;
import cn.maple.shiro.mapper.GXPermissionsMapper;
import cn.maple.shiro.service.GXAdminPermissionsService;
import cn.maple.shiro.service.GXPermissionsService;
import cn.maple.shiro.service.GXRolePermissionsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class GXPermissionsServiceImpl
        extends GXDBBaseServiceImpl<GXPermissionsEntity, GXPermissionsMapper, GXPermissionsDao, GXPermissionsResDto>
        implements GXPermissionsService {
    @Resource
    private GXAdminPermissionsService adminPermissionsService;

    @Resource
    private GXRolePermissionsService rolePermissionsService;

    @Override
    public Set<String> getAdminAllPermissions(Long adminId) {
        Set<String> adminPermissions = adminPermissionsService.getPermissionsByAdminId(adminId);
        Set<String> rolePermissions = rolePermissionsService.getPermissionsByAdminId(adminId);
        final Collection<String> permissions = CollUtil.addAll(adminPermissions, rolePermissions);
        return new HashSet<>(permissions);
    }
}
