package com.geoxus.shiro.builder;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.constant.GXCommonConstants;
import org.apache.ibatis.jdbc.SQL;

public class GXAdminRoleBuilder implements GXBaseBuilder {
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
        return "admin_role";
    }

    /**
     * 获取管理员的角色列表
     *
     * @param condition 查询条件
     * @return SQL语句
     */
    public String getAdminRoles(Dict condition) {
        final SQL sql = new SQL().SELECT("role.code").FROM("admin_role")
                .INNER_JOIN("role ON role.id = admin_role.role_id");
        condition.forEach((column, value1) -> {
            final String value = Convert.toStr(value1);
            String template = "{} " + GXBaseBuilderConstants.STR_EQ;
            if (ReUtil.isMatch(GXCommonConstants.DIGITAL_REGULAR_EXPRESSION, value)) {
                template = "{} " + GXBaseBuilderConstants.NUMBER_EQ;
            }
            sql.WHERE(CharSequenceUtil.format(template, column, value));
        });
        return sql.toString();
    }
}
