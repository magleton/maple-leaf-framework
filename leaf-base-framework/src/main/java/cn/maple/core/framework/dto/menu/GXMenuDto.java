package cn.maple.core.framework.dto.menu;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GXMenuDto extends GXBaseDto {
    /**
     * 菜单名字
     */
    private String menuName;

    /**
     * 菜单code
     */
    private String menuCode;

    /**
     * 菜单类型  1 : 目录  2 : 菜单  3 : 按钮
     */
    private Integer menuType;

    /**
     * 菜单URL
     */
    private String menuUrl;

    /**
     * 子菜单列表 带有权限配置
     */
    private List<GXMenuPermissionDto> permissionDtoList;
}
