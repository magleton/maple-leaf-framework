package com.geoxus.shiro.dao;

import com.geoxus.core.datasource.dao.GXBaseDao;
import com.geoxus.shiro.dto.res.GXRolePermissionsResDto;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import com.geoxus.shiro.mapper.GXRolePermissionsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXRolePermissionsDao extends GXBaseDao<GXRolePermissionsEntity, GXRolePermissionsMapper, GXRolePermissionsResDto> {
}
