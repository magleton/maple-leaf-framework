package com.geoxus.dto.protocol.req;

import com.geoxus.core.common.annotation.GXSingleFieldToDbJsonFieldAnnotation;
import com.geoxus.core.common.dto.protocol.GXBaseReqProtocol;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserReqProtocol implements GXBaseReqProtocol {
    private static final Integer CORE_MODEL_ID = 1;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String address;

    //@GXValidateExtDataAnnotation(tableName = "users")
    private String ext;

    @GXSingleFieldToDbJsonFieldAnnotation(dbFieldName = "author")
    private String author;

    @GXSingleFieldToDbJsonFieldAnnotation(dbFieldName = "sub_title")
    private String subTitle;

    @Override
    public void repair() {
        System.out.println("自定义的修复数据");
        subTitle = "我的副标题哈";
    }
}
