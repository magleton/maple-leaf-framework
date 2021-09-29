package com.geoxus.order.dto.protocol.res;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.dto.protocol.res.GXBaseResProtocol;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResProtocol extends GXBaseResProtocol {
    private String username;

    private String ext;

    private String name;

    private List<Dict> addresses;

    @Override
    public void processResValue() {
        username = "Tommy-Tank";
    }
}
