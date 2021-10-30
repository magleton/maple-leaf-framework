package cn.maple.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.maple.core.framework.annotation.GXFieldComment;
import cn.maple.core.framework.dto.GXBaseData;
import cn.maple.shiro.constant.GXRoleConstant;
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
