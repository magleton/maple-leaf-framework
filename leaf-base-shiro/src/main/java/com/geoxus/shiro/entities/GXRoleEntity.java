package com.geoxus.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.common.dto.GXBaseData;
import com.geoxus.shiro.constant.GXRoleConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@TableName(GXRoleConstant.TABLE_NAME)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXRoleEntity extends GXBaseData {
    @GXFieldComment(zhDesc = "主键ID")
    private Integer id;

    @GXFieldComment(zhDesc = "角色名字")
    private String roleName;

    @GXFieldComment("角色code")
    private String code;

    @GXFieldComment("租户ID")
    private String tenantId;

    @GXFieldComment(zhDesc = "扩展预留信息")
    private String ext;
}
