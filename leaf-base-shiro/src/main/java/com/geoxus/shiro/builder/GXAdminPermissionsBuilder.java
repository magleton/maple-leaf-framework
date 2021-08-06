package com.geoxus.shiro.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

public class GXAdminPermissionsBuilder implements GXBaseBuilder {
    /**
     * 列表
     *
     * @param param 参数
     * @return String
     */
    @Override
    public String listOrSearch(Dict param) {
        return null;
    }

    /**
     * 详情
     *
     * @param param 参数
     * @return String
     */
    @Override
    public String detail(Dict param) {
        return null;
    }

    /**
     * 数据配置的模型标识
     *
     * @return String
     */
    @Override
    public String getModelIdentificationValue() {
        return "admin_permissions";
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
