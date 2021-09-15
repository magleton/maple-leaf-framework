package com.geoxus.shiro.dto.req;

import com.geoxus.core.common.annotation.GXMergeSingleFieldAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.common.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXRolePermissionsConstant;
import lombok.Data;

@Data
public class GXRolePermissionsReqDto extends GXBaseDto {
    /**
     * 核心模型ID
     */
    private static final int CORE_MODEL_ID = GXRolePermissionsConstant.CORE_MODEL_ID;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 权限ID
     */
    private Integer permissionId;

    /**
     * 扩展数据
     */
    @GXValidateExtDataAnnotation(tableName = GXRolePermissionsConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleFieldAnnotation(fieldName = "author")
    private String author;
}
