package com.geoxus.order.dto;

import com.geoxus.common.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserReqBusinessDto extends GXBaseDto {
    private static final long serialVersionUID = -3616191680110988112L;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String address;

    @Override
    public void verify() {
        System.out.println("先自定义验证一下");
        super.verify();
    }

    @Override
    public void repair() {
        this.username = "我的固定名字";
    }
}
