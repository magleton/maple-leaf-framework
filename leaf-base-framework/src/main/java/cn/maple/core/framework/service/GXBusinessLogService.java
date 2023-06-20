package cn.maple.core.framework.service;

import cn.maple.core.framework.dto.inner.GXBusinessLogDto;

/**
 * 保存业务日志信息
 *
 * @author 子曦 godbolt@163.com
 */
public interface GXBusinessLogService {
    /**
     * 自定义保存业务日志信息
     * 可以将data参数转换为自己需要的dto
     *
     * @param businessLogDto 搜集的日志参数
     */
    void saveBusinessLog(GXBusinessLogDto businessLogDto);

    /**
     * 获取当前登录用户
     *
     * @return String 当前登录用户名
     */
    String getUserName();
}
