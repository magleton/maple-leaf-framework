package com.geoxus.core.common.dto.protocol;

/**
 * 用于接收用户请求信息
 * 并对请求信息进行一些额外处理
 */
public interface GXBaseReqProtocol {
    /**
     * @author britton
     * 对请求参数进行补充校验
     */
    default void verify() {
    }

    /**
     * @author britton
     * 对参数进行补充修复(自动填充一些信息)
     */
    default void repair() {
    }
}
