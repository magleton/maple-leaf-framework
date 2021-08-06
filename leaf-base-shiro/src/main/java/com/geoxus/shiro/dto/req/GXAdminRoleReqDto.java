package com.geoxus.shiro.dto.req;

import com.geoxus.core.common.annotation.GXMergeSingleFieldToJSONFieldAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXAdminRoleConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXAdminRoleReqDto extends GXBaseDto {
    /**
     * 核心模型ID
     */
    private static final int CORE_MODEL_ID = GXAdminRoleConstant.CORE_MODEL_ID;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 管理员ID
     */
    private Integer adminId;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 扩展数据
     */
    @GXValidateExtDataAnnotation(tableName = GXAdminRoleConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleFieldToJSONFieldAnnotation
    private String author;
}
