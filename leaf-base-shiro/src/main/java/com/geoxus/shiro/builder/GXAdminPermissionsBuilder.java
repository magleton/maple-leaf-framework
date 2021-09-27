package com.geoxus.shiro.builder;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.framework.builder.GXBaseBuilder;
import com.geoxus.shiro.constant.GXAdminPermissionsConstant;
import org.apache.ibatis.jdbc.SQL;

public class GXAdminPermissionsBuilder implements GXBaseBuilder {
    /**
     * 数据配置的模型标识
     *
     * @return String
     */
    @Override
    public String getModelIdentificationValue() {
        return GXAdminPermissionsConstant.MODEL_IDENTIFICATION_VALUE;
    }

    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return SQL语句
     */
    public String getPermissionsByAdminId(Long adminId) {
        final SQL sql = new SQL().SELECT("permissions.code").FROM("permissions")
                .INNER_JOIN("admin_permissions ON admin_permissions.permission_id = permissions.id")
                .WHERE(CharSequenceUtil.format("admin_permissions.admin_id = {}", adminId));
        return sql.toString();
    }
}
