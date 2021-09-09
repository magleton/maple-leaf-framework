package com.geoxus.dto.protocol;

import com.geoxus.core.common.dto.protocol.GXBaseReqProtocol;
import lombok.Data;

@Data
public class UserReqProtocol implements GXBaseReqProtocol {
    private static final Integer CORE_MODEL_ID = 1;
    
    private String username;

    private String password;

    private String address;
}
