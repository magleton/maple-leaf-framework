package cn.maple.shiro.service;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.service.GXDBBaseService;
import cn.maple.shiro.dao.GXAdminRoleDao;
import cn.maple.shiro.dto.res.GXAdminRoleResDto;
import cn.maple.shiro.entities.GXAdminRolesEntity;
import cn.maple.shiro.mapper.GXAdminRoleMapper;

import java.util.HashSet;
import java.util.Set;

public interface GXAdminRoleService extends
        GXDBBaseService<GXAdminRolesEntity, GXAdminRoleMapper, GXAdminRoleDao, GXAdminRoleResDto> {
    /**
     * 获取当前人的角色列表
     *
     * @param adminId 为NULL是获取当前登录人的
     * @return Dict
     */
    default Set<String> getAdminRoles(Long adminId) {
        return new HashSet<>(0);
    }

    /**
     * 创建数据
     *
     * @param target 目标实体
     * @param param  额外参数
     * @return long
     */
    long create(GXAdminRolesEntity target, Dict param);
}
