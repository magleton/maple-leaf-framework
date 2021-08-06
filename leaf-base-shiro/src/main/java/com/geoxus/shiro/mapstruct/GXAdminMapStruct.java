package com.geoxus.shiro.mapstruct;

import com.geoxus.shiro.dto.req.GXAdminReqDto;
import com.geoxus.shiro.entities.GXAdminEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminMapStruct {
    GXAdminReqDto entityToDto(GXAdminEntity entity);

    GXAdminEntity dtoToEntity(GXAdminReqDto permissionsReqDto);
}
