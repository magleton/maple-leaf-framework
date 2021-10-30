package cn.maple.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.maple.core.framework.annotation.GXFieldComment;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.shiro.constant.GXRolePermissionsConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(GXRolePermissionsConstant.TABLE_NAME)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXRolePermissionsEntity extends GXBaseEntity implements Serializable {
    @GXFieldComment(zhDesc = "主键ID")
    private Integer id;

    @GXFieldComment(zhDesc = "角色ID")
    private Integer roleId;

    @GXFieldComment(zhDesc = "权限ID")
    private Integer permissionId;

    @GXFieldComment("租户ID")
    private String tenantId;

    @GXFieldComment(zhDesc = "扩展预留信息")
    private String ext;
}
