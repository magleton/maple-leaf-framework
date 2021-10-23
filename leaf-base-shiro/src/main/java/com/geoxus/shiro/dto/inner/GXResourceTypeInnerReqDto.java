package com.geoxus.shiro.dto.inner;

import com.geoxus.core.framework.annotation.GXFieldComment;
import com.geoxus.core.framework.dto.GXBaseDto;
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
