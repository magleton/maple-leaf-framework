package cn.maple.core.framework.dto.inner.permission;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GXBasePermissionInnerDto extends GXBaseDto {
    /**
     * 权限名字
     */
    private String permissionName;

    /**
     * 权限码
     */
    private String permissionCode;

    /**
     * 权限所属模块名字
     */
    private String moduleName;

    /**
     * 权限所属模块code
     */
    private String moduleCode;
}
