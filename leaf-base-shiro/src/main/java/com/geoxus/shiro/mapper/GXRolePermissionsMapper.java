package com.geoxus.shiro.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.shiro.builder.GXRolePermissionsBuilder;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Mapper
public interface GXRolePermissionsMapper extends GXBaseMapper<GXRolePermissionsEntity> {
    @SelectProvider(type = GXRolePermissionsBuilder.class, method = "getPermissionsByAdminId")
    Set<String> getPermissionsByAdminId(Long adminId);
}
