package cn.maple.shiro.service.impl;

import cn.maple.core.datasource.service.impl.GXDBBaseServiceImpl;
import cn.maple.shiro.dao.GXRolePermissionsDao;
import cn.maple.shiro.dto.res.GXRolePermissionsResDto;
import cn.maple.shiro.entities.GXRolePermissionsEntity;
import cn.maple.shiro.mapper.GXRolePermissionsMapper;
import cn.maple.shiro.service.GXRolePermissionsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXRolePermissionsServiceImpl
        extends GXDBBaseServiceImpl<GXRolePermissionsEntity, GXRolePermissionsMapper, GXRolePermissionsDao, GXRolePermissionsResDto>
        implements GXRolePermissionsService {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    @Override
    public Set<String> getPermissionsByAdminId(Long adminId) {
        return getBaseMapper().getPermissionsByAdminId(adminId);
    }
}
