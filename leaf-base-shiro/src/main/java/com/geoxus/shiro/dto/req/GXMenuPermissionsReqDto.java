package com.geoxus.shiro.dto.req;

import com.geoxus.core.framework.annotation.GXValidateExtData;
import com.geoxus.core.framework.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXMenuPermissionsConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXMenuPermissionsReqDto extends GXBaseDto {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 菜单ID
     */
    private String menuId;

    /**
     * 权限ID
     */
    private String permissionId;

    /**
     * 权限码
     */
    private String permissionCode;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 扩展预留信息
     */
    @GXValidateExtData(tableName = GXMenuPermissionsConstant.TABLE_NAME)
    private String ext;
}
