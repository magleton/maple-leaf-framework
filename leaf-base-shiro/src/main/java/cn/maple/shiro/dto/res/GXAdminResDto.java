package cn.maple.shiro.dto.res;

import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXAdminResDto extends GXBaseResDto {
    /**
     * 登录用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;
}
