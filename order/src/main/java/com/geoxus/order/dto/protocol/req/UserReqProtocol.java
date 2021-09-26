package com.geoxus.order.dto.protocol.req;

import com.geoxus.common.annotation.GXMergeSingleField;
import com.geoxus.common.dto.protocol.req.GXBaseReqProtocol;
import com.geoxus.core.framework.service.impl.GXValidateJsonFieldServiceImpl;
import com.geoxus.order.service.impl.MyGXValidateJsonFieldServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserReqProtocol extends GXBaseReqProtocol {
    private static final long serialVersionUID = -7741423300357249210L;

    /**
     * 主键ID
     */
    private Integer id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String address;

    //@GXValidateExtData(tableName = "users")
    private String ext;

    @GXMergeSingleField(fieldName = "author", service = MyGXValidateJsonFieldServiceImpl.class)
    private String author;

    @GXMergeSingleField(fieldName = "sub_title", service = GXValidateJsonFieldServiceImpl.class)
    private String subTitle;

    @Override
    public void repair() {
        System.out.println("自定义的修复数据");
        subTitle = "我的副标题哈";
    }
}
