package com.geoxus.shiro.dao;

import com.geoxus.core.datasource.dao.GXBaseDao;
import com.geoxus.shiro.entities.GXPermissionsEntity;
import com.geoxus.shiro.mapper.GXPermissionsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXPermissionsDao extends GXBaseDao<GXPermissionsMapper, GXPermissionsEntity> {
}
