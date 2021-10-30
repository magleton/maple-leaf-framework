package cn.maple.shiro.dao;

import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.shiro.dto.res.GXAdminPermissionsResDto;
import cn.maple.shiro.entities.GXAdminPermissionsEntity;
import cn.maple.shiro.mapper.GXAdminPermissionsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXAdminPermissionsDao extends GXBaseDao<GXAdminPermissionsEntity, GXAdminPermissionsMapper, GXAdminPermissionsResDto> {
}
