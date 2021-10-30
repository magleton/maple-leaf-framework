package cn.maple.shiro.dto.req;

import cn.maple.core.framework.annotation.GXValidateExtData;
import cn.maple.core.framework.dto.GXBaseDto;
import cn.maple.shiro.constant.GXMenuConstant;
import cn.maple.shiro.dto.inner.GXResourceTypeInnerReqDto;
import cn.maple.shiro.enums.GXMenuTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXMenuReqDto extends GXBaseDto {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 菜单名字
     */
    private String name;

    /**
     * 父级ID
     */
    private String pid;

    /**
     * 按钮类型
     */
    private GXMenuTypeEnum type;

    /**
     * 菜单资源
     */
    private GXResourceTypeInnerReqDto resource;

    /**
     * 资源Code
     */
    private String code;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 扩展预留信息
     */
    @GXValidateExtData(tableName = GXMenuConstant.TABLE_NAME)
    private String ext;
}
