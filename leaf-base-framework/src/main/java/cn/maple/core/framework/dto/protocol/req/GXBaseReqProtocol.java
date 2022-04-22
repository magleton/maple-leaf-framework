package cn.maple.core.framework.dto.protocol.req;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;

/**
 * @author britton
 * @version 1.0
 * @since 2021-09-15
 * <p>
 * 用于接收用户请求信息
 * 并对请求信息进行一些额外处理
 */
public abstract class GXBaseReqProtocol extends GXBaseReqDto {
    /**
     * @author britton
     * 在验证请求参数之前进行数据修复(自动填充一些信息)
     */
    protected void beforeRepair() {
    }

    /**
     * @author britton
     * 参数
     * 验证请求参数之后进行数据修复(自动填充一些信息)
     */
    protected void afterRepair() {
    }

    /**
     * 获取用户的登录信息
     *
     * @param tokenName      token的名字
     * @param tokenSecretKey token密钥
     * @return 解密之后的登录信息
     */
    protected Dict getLoginCredentials(String tokenName, String tokenSecretKey) {
        return GXCurrentRequestContextUtils.getLoginCredentials(tokenName, tokenSecretKey);
    }
}
