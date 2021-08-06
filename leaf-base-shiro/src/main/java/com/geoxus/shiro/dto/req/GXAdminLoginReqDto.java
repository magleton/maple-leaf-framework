package com.geoxus.shiro.dto.req;

import com.geoxus.core.common.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXAdminLoginReqDto extends GXBaseDto {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码必填")
    @NotEmpty(message = "密码必填")
    private String password;
}
