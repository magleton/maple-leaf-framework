package com.geoxus.aggregate.dto.protocol.req;

import com.geoxus.aggregate.service.impl.MyGXValidateJSONFieldService;
import com.geoxus.core.framework.annotation.GXMergeSingleField;
import com.geoxus.core.framework.annotation.GXValidateExtData;
import lombok.Data;

@Data
public class UserReqProtocol {
    private String userName;

    private String password;

    @GXMergeSingleField(tableName = "", fieldName = "author", service = MyGXValidateJSONFieldService.class)
    private String author;

    @GXMergeSingleField(tableName = "", fieldName = "sub_title")
    private String subTitle;

    @GXValidateExtData(tableName = "")
    private String ext;
}
