package com.geoxus.shiro.dto.inner;

import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.common.dto.GXBaseDto;
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
