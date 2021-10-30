package cn.maple.shiro.dao;

import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.shiro.dto.res.GXAdminResDto;
import cn.maple.shiro.entities.GXAdminEntity;
import cn.maple.shiro.mapper.GXAdminMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GXAdminDao extends GXBaseDao<GXAdminEntity, GXAdminMapper, GXAdminResDto> {
}
