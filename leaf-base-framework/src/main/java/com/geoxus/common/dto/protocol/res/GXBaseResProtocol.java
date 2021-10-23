package com.geoxus.common.dto.protocol.res;

import com.geoxus.common.dto.GXBaseDto;

/**
 * @author britton
 * @version 1.0
 * @since 2021-09-15
 * <p>
 * 用于处理返回的用户数据
 * 并对返回数据进行一些额外的逻辑处理
 */
public abstract class GXBaseResProtocol extends GXBaseDto {
    /**
     * @author britton
     * @since 2021-09-15
     * 对返回数据进行自定义处理
     */
    public void processResValue() {
    }
}
