package com.geoxus.dto.protocol.req;

import com.geoxus.core.common.annotation.GXMergeSingleFieldToJSONFieldAnnotation;
import com.geoxus.core.common.dto.protocol.GXBaseReqProtocol;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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

    private String ext;

    @GXMergeSingleFieldToJSONFieldAnnotation(dbFieldName = "author")
    @Length(min = 20, message = "不能少于20个字符")
    private String author;

    @GXMergeSingleFieldToJSONFieldAnnotation(dbFieldName = "sub_title")
    private String subTitle;

    @Override
    public void repair() {
        System.out.println("自定义的修复数据");
        subTitle = "我的副标题哈";
    }
}
