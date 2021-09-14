package com.geoxus.common.dto.protocol.req;

import com.geoxus.common.dto.GXBaseDto;

/**
 * 用于接收用户请求信息
 * 并对请求信息进行一些额外处理
 */
public abstract class GXBaseReqProtocol extends GXBaseDto {
    /**
     * @author britton
     * 对请求参数进行补充校验
     */
    public void verify() {
    }

    /**
     * @author britton
     * 对参数进行补充修复(自动填充一些信息)
     */
    public void repair() {
    }
}
