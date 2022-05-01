package cn.maple.shiro.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GXShiroCommonUtils {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(GXShiroCommonUtils.class);

    private GXShiroCommonUtils() {
    }

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
            LOG.error(e.getMessage(), e);
        }
        return 0;
    }
}
