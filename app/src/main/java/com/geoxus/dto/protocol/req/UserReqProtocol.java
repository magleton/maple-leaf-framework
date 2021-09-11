package com.geoxus.dto.protocol.req;

import com.geoxus.core.common.annotation.GXSingleFieldToDbJsonFieldAnnotation;
import com.geoxus.core.common.dto.protocol.req.GXBaseReqProtocol;
import com.geoxus.service.impl.MyGXValidateJsonFieldServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserReqProtocol extends GXBaseReqProtocol {
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

    //@GXValidateExtDataAnnotation(tableName = "users")
    private String ext;

    @GXSingleFieldToDbJsonFieldAnnotation(fieldName = "author", service = MyGXValidateJsonFieldServiceImpl.class)
    private String author;

    @GXSingleFieldToDbJsonFieldAnnotation(fieldName = "sub_title")
    private String subTitle;

    @Override
    public void repair() {
        System.out.println("自定义的修复数据");
        subTitle = "我的副标题哈";
    }
}
