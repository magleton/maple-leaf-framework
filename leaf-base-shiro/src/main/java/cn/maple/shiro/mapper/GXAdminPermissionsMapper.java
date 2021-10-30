package cn.maple.shiro.mapper;

import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.shiro.builder.GXAdminPermissionsBuilder;
import cn.maple.shiro.dto.res.GXAdminPermissionsResDto;
import cn.maple.shiro.entities.GXAdminPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper
@Component
public interface GXAdminPermissionsMapper extends GXBaseMapper<GXAdminPermissionsEntity, GXAdminPermissionsResDto> {
    @SelectProvider(type = GXAdminPermissionsBuilder.class, method = "getPermissionsByAdminId")
    Set<String> getPermissionsByAdminId(Long adminId);
}
