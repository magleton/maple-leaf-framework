package com.geoxus.dto.protocol.req;

import com.geoxus.core.common.annotation.GXMergeSingleFieldToJSONFieldAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
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

    @GXValidateExtDataAnnotation(tableName = "user")
    private String ext;

    @GXMergeSingleFieldToJSONFieldAnnotation(dbFieldName = "author")
    private String author;

    @GXMergeSingleFieldToJSONFieldAnnotation(dbFieldName = "age")
    private Integer age;
}
