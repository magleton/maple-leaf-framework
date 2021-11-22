package cn.maple.core.framework.dto.menu;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GXMenuPermissionDto extends GXBaseDto {
    /**
     * 权限名字
     */
    private String permissionName;

    /**
     * 权限码
     */
    private String permissionCode;

    /**
     * 控制器名字
     */
    private String controllerName;

    /**
     * 动作名字
     */
    private String actionName;

    /**
     * 菜单名字
     */
    private String menuName;

    /**
     * 菜单URL
     */
    private String menuUrl;

    /**
     * 菜单所属分组  eg : 查看需要列表和详情 , 则列表和详情应该归为一组
     */
    private String menuGroup;

    /**
     * 菜单类型  1 : 目录  2 : 菜单  3 : 按钮
     */
    private Integer menuType;
}
