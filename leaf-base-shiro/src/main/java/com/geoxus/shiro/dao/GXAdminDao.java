package com.geoxus.shiro.dao;

import com.geoxus.core.datasource.dao.GXBaseDao;
import com.geoxus.shiro.dto.res.GXAdminResDto;
import com.geoxus.shiro.entities.GXAdminEntity;
import com.geoxus.shiro.mapper.GXAdminMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXAdminDao extends GXBaseDao<GXAdminEntity, GXAdminMapper, GXAdminResDto> {
}
