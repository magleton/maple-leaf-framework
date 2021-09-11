package com.geoxus.dto.protocol.res;

import com.geoxus.core.common.dto.protocol.res.GXBaseResProtocol;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResProtocol extends GXBaseResProtocol {
    private String username;

    private String ext;

    @Override
    public void processResValue() {
        username = "Tommy-Tank";
    }
}
