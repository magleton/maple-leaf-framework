package com.geoxus.order.common.dto.protocol.res;

import com.geoxus.common.dto.protocol.res.GXBaseResProtocol;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResProtocol extends GXBaseResProtocol {
    private String username;

    private String ext;

    private String name;

    @Override
    public void processResValue() {
        username = "Tommy-Tank";
    }
}
