package com.geoxus.shiro.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.shiro.builder.GXAdminPermissionsBuilder;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper
@Component
public interface GXAdminPermissionsMapper extends GXBaseMapper<GXAdminPermissionsEntity> {
    @SelectProvider(type = GXAdminPermissionsBuilder.class, method = "getPermissionsByAdminId")
    Set<String> getPermissionsByAdminId(Long adminId);
}
