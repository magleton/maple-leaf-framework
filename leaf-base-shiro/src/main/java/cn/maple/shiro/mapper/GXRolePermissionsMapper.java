package cn.maple.shiro.mapper;

import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.shiro.builder.GXRolePermissionsBuilder;
import cn.maple.shiro.dto.res.GXRolePermissionsResDto;
import cn.maple.shiro.entities.GXRolePermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Mapper
public interface GXRolePermissionsMapper extends GXBaseMapper<GXRolePermissionsEntity, GXRolePermissionsResDto> {
    @SelectProvider(type = GXRolePermissionsBuilder.class, method = "getPermissionsByAdminId")
    Set<String> getPermissionsByAdminId(Long adminId);
}
