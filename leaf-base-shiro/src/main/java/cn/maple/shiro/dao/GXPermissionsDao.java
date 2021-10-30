package cn.maple.shiro.dao;

import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.shiro.dto.res.GXPermissionsResDto;
import cn.maple.shiro.entities.GXPermissionsEntity;
import cn.maple.shiro.mapper.GXPermissionsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXPermissionsDao extends GXBaseDao<GXPermissionsEntity, GXPermissionsMapper, GXPermissionsResDto> {
}
