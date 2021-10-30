package cn.maple.shiro.dto.req;

import cn.maple.core.framework.annotation.GXMergeSingleField;
import cn.maple.core.framework.annotation.GXValidateExtData;
import cn.maple.core.framework.dto.GXBaseDto;
import cn.maple.shiro.constant.GXRoleConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXRoleReqDto extends GXBaseDto {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 角色code
     */
    private String code;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 扩展数据
     */
    @GXValidateExtData(tableName = GXRoleConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleField(fieldName = "author")
    private String author;
}
