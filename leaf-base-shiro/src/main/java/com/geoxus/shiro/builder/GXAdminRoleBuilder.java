package com.geoxus.shiro.builder;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReUtil;
import com.geoxus.core.datasource.builder.GXBaseBuilder;
import com.geoxus.core.datasource.constant.GXBaseBuilderConstant;
import com.geoxus.core.framework.constant.GXCommonConstant;
import com.geoxus.shiro.constant.GXAdminRoleConstant;
import org.apache.ibatis.jdbc.SQL;

public class GXAdminRoleBuilder implements GXBaseBuilder {
    /**
     * 数据配置的模型标识
     *
     * @return String
     */
    @Override
    public String getModelIdentificationValue() {
        return GXAdminRoleConstant.MODEL_IDENTIFICATION_VALUE;
    }

    /**
     * 获取管理员的角色列表
     *
     * @param condition 查询条件
     * @return SQL语句
     */
    public String getAdminRoles(Dict condition) {
        final SQL sql = new SQL().SELECT("role.code").FROM(GXAdminRoleConstant.TABLE_NAME)
                .INNER_JOIN("role ON role.id = admin_role.role_id");
        condition.forEach((column, value1) -> {
            final String value = Convert.toStr(value1);
            String template = "{} " + GXBaseBuilderConstant.STR_EQ;
            if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value)) {
                template = "{} " + GXBaseBuilderConstant.NUMBER_EQ;
            }
            sql.WHERE(CharSequenceUtil.format(template, column, value));
        });
        return sql.toString();
    }
}
