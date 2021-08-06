package com.geoxus.shiro.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GXShiroCommonUtils {
    /**
     * 获取当前登录用户的ID
     * 0  : 表示普通用户
     * >0 : 确定管理员
     *
     * @return Long
     */
    public static long getCurrentSessionUserId() {
        try {
            return GXShiroUtils.getAdminId();
        } catch (Exception e) {
            GXShiroCommonUtils.getLogger(GXShiroCommonUtils.class).error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 获取Logger对象
     *
     * @param clazz Class
     * @return Logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
