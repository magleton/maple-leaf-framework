package com.geoxus.shiro.dto.req;

import com.geoxus.common.annotation.GXMergeSingleField;
import com.geoxus.common.annotation.GXValidateExtData;
import com.geoxus.common.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXAdminRoleConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXAdminRolesReqDto extends GXBaseDto {
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
     * 租户ID
     */
    private String tenantId;

    /**
     * 扩展数据
     */
    @GXValidateExtData(tableName = GXAdminRoleConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleField(fieldName = "author")
    private String author;
}
