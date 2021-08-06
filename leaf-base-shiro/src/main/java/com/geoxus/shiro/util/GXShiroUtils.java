package com.geoxus.shiro.util;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.constant.GXTokenConstants;
import com.geoxus.core.common.util.GXCommonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class GXShiroUtils {
    private GXShiroUtils() {
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Dict getAdminData() {
        final Object principal = SecurityUtils.getSubject().getPrincipal();
        if (null != principal) {
            return (Dict) SecurityUtils.getSubject().getPrincipal();
        }
        return Dict.create();
    }

    public static Long getAdminId() {
        if (getAdminData().isEmpty()) {
            return 0L;
        }
        Long adminId = getAdminData().getLong(GXTokenConstants.ADMIN_ID);
        if (null == adminId) {
            adminId = getAdminData().getLong(GXCommonUtils.toCamelCase(GXTokenConstants.ADMIN_ID));
        }
        return null == adminId ? 0 : adminId;
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }
}
