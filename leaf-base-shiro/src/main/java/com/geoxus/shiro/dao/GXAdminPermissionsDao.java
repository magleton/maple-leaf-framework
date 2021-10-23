package com.geoxus.shiro.dao;

import com.geoxus.core.datasource.dao.GXBaseDao;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import com.geoxus.shiro.mapper.GXAdminPermissionsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXAdminPermissionsDao extends GXBaseDao<GXAdminPermissionsMapper, GXAdminPermissionsEntity> {
}
