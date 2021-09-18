package com.geoxus.aggregate.dto.protocol.req;

import com.geoxus.common.annotation.GXMergeSingleField;
import com.geoxus.common.annotation.GXValidateExtData;
import lombok.Data;

@Data
public class UserReqProtocol {
    private String userName;

    private String password;

    @GXMergeSingleField(tableName = "", fieldName = "author")
    private String author;

    @GXMergeSingleField(tableName = "", fieldName = "sub_title")
    private String subTitle;

    @GXValidateExtData(tableName = "")
    private String ext;
}
