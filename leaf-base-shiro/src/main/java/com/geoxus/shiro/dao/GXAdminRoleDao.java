package com.geoxus.shiro.dao;

import com.geoxus.core.dao.GXBaseDao;
import com.geoxus.shiro.entities.GXAdminRolesEntity;
import com.geoxus.shiro.mapper.GXAdminRoleMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXAdminRoleDao extends GXBaseDao<GXAdminRoleMapper, GXAdminRolesEntity> {
}
