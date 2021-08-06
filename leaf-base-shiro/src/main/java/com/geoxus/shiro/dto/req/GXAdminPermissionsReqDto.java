package com.geoxus.shiro.dto.req;

import com.geoxus.core.common.annotation.GXMergeSingleFieldToJSONFieldAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXAdminPermissionsConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXAdminPermissionsReqDto extends GXBaseDto {
    /**
     * 核心模型ID
     */
    private static final int CORE_MODEL_ID = GXAdminPermissionsConstant.CORE_MODEL_ID;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 管理员ID
     */
    private Integer adminId;

    /**
     * 权限ID
     */
    private Integer permissionId;

    /**
     * 扩展数据
     */
    @GXValidateExtDataAnnotation(tableName = GXAdminPermissionsConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleFieldToJSONFieldAnnotation
    private String author;
}
