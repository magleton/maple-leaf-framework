package com.geoxus.core.common.dto.protocol;

import com.geoxus.core.common.dto.GXBaseDto;

/**
 * 用于处理返回的用户数据
 * 并对返回数据进行一些额外处理
 */
public abstract class GXBaseResProtocol extends GXBaseDto {
    /**
     * @author britton
     * 对返回数据进行自定义处理
     */
    public void processResValue() {
    }
}
