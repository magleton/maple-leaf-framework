package cn.maple.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.maple.core.framework.annotation.GXFieldComment;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.shiro.constant.GXMenuConstant;
import cn.maple.shiro.dto.inner.GXResourceTypeInnerReqDto;
import cn.maple.shiro.enums.GXMenuTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(GXMenuConstant.TABLE_NAME)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXMenuEntity extends GXBaseEntity implements Serializable {
    @GXFieldComment(zhDesc = "主键ID")
    private Integer id;

    @GXFieldComment(zhDesc = "菜单名字")
    private String name;

    @GXFieldComment(zhDesc = "父级ID")
    private String pid;

    @GXFieldComment(zhDesc = "按钮类型")
    private GXMenuTypeEnum type;

    @GXFieldComment(zhDesc = "菜单资源")
    private GXResourceTypeInnerReqDto resource;

    @GXFieldComment(zhDesc = "资源Code")
    private String code;

    @GXFieldComment("租户ID")
    private String tenantId;

    @GXFieldComment(zhDesc = "扩展预留信息")
    private String ext;
}
