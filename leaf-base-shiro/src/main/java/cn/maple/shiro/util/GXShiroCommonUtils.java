package cn.maple.shiro.util;

import cn.maple.core.framework.util.GXCommonUtils;

public class GXShiroCommonUtils {
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
            GXCommonUtils.getLogger(GXShiroCommonUtils.class).error(e.getMessage(), e);
        }
        return 0;
    }
}
