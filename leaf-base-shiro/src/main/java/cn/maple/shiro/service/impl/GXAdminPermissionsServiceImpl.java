package cn.maple.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.service.impl.GXDBBaseServiceImpl;
import cn.maple.shiro.dao.GXAdminPermissionsDao;
import cn.maple.shiro.dto.res.GXAdminPermissionsResDto;
import cn.maple.shiro.entities.GXAdminPermissionsEntity;
import cn.maple.shiro.mapper.GXAdminPermissionsMapper;
import cn.maple.shiro.service.GXAdminPermissionsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminPermissionsServiceImpl extends
        GXDBBaseServiceImpl<GXAdminPermissionsEntity, GXAdminPermissionsMapper, GXAdminPermissionsDao, GXAdminPermissionsResDto>
        implements GXAdminPermissionsService {
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

    public long create(GXAdminPermissionsEntity target, Dict param) {
        baseDao.save(target);
        return target.getId();
    }
}
