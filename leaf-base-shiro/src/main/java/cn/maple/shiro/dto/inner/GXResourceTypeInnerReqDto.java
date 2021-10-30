package cn.maple.shiro.dto.inner;

import cn.maple.core.framework.annotation.GXFieldComment;
import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXResourceTypeInnerReqDto extends GXBaseDto {
    @GXFieldComment(zhDesc = "控制器名字")
    private String controllerName;

    @GXFieldComment(zhDesc = "动作名字")
    private String actionName;
}
