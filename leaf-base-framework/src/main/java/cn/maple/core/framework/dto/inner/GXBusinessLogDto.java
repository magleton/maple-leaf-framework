package cn.maple.core.framework.dto.inner;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务日志请求DTO
 *
 * @author 子曦 godbolt@163.com
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("all")
@Data
public class GXBusinessLogDto extends GXBaseDto {
    /**
     * 业务的日志名字
     */
    private String businessName;

    /**
     * 业务的日志描述
     */
    private String businessDescription;

    /**
     * 调用的方法名字
     */
    private String methodName;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求用户名字
     */
    private String userName;

    /**
     * 请求IP地址
     */
    private String ip;

    /**
     * 执行时间
     */
    private Long executionTime;

    /**
     * 请求时间
     */
    private Long requestAt;
}
