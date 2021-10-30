package cn.maple.shiro.dao;

import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.shiro.dto.res.GXRolePermissionsResDto;
import cn.maple.shiro.entities.GXRolePermissionsEntity;
import cn.maple.shiro.mapper.GXRolePermissionsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXRolePermissionsDao extends GXBaseDao<GXRolePermissionsEntity, GXRolePermissionsMapper, GXRolePermissionsResDto> {
}
