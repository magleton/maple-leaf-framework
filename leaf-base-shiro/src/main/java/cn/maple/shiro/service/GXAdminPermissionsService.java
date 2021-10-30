package cn.maple.shiro.service;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.service.GXDBBaseService;
import cn.maple.shiro.dao.GXAdminPermissionsDao;
import cn.maple.shiro.dto.res.GXAdminPermissionsResDto;
import cn.maple.shiro.entities.GXAdminPermissionsEntity;
import cn.maple.shiro.mapper.GXAdminPermissionsMapper;

import java.util.Set;

public interface GXAdminPermissionsService extends
        GXDBBaseService<GXAdminPermissionsEntity, GXAdminPermissionsMapper, GXAdminPermissionsDao, GXAdminPermissionsResDto> {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    Set<String> getPermissionsByAdminId(Long adminId);

    /**
     * 创建数据
     *
     * @param target 目标实体
     * @param param  额外参数
     * @return long
     */
    long create(GXAdminPermissionsEntity target, Dict param);
}
