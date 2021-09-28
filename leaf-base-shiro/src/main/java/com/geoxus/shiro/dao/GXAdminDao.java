package com.geoxus.shiro.dao;

import com.geoxus.core.dao.GXBaseDao;
import com.geoxus.shiro.entities.GXAdminEntity;
import com.geoxus.shiro.mapper.GXAdminMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXAdminDao extends GXBaseDao<GXAdminMapper, GXAdminEntity> {
}
