package cn.maple.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.service.impl.GXDBBaseServiceImpl;
import cn.maple.shiro.dao.GXAdminRoleDao;
import cn.maple.shiro.dto.res.GXAdminRoleResDto;
import cn.maple.shiro.entities.GXAdminRolesEntity;
import cn.maple.shiro.mapper.GXAdminRoleMapper;
import cn.maple.shiro.service.GXAdminRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminRoleServiceImpl extends
        GXDBBaseServiceImpl<GXAdminRolesEntity, GXAdminRoleMapper, GXAdminRoleDao, GXAdminRoleResDto> implements GXAdminRoleService {
    @Override
    public Set<String> getAdminRoles(Long adminId) {
        final Dict condition = Dict.create().set("admin_id", adminId);
        return getBaseMapper().getAdminRoles(condition);
    }

    public long create(GXAdminRolesEntity target, Dict param) {
        baseDao.save(target);
        return target.getId();
    }
}
