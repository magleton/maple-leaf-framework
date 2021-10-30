package cn.maple.shiro.dao;

import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.shiro.dto.res.GXAdminRoleResDto;
import cn.maple.shiro.entities.GXAdminRolesEntity;
import cn.maple.shiro.mapper.GXAdminRoleMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXAdminRoleDao extends GXBaseDao<GXAdminRolesEntity, GXAdminRoleMapper, GXAdminRoleResDto> {
}
