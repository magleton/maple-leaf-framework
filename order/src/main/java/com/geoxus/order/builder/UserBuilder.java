package com.geoxus.order.builder;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstant;
import org.apache.ibatis.jdbc.SQL;

public class UserBuilder implements GXBaseBuilder {
    public String getUserInfo(Long userId) {
        return new SQL().FROM("user").SELECT("*").WHERE("id = " + userId).toString();
    }

    @Override
    public <R> String listOrSearchPage(IPage<R> page, Dict param) {
        SQL sql = new SQL().FROM("user").SELECT("user.username , t.name")
                .INNER_JOIN("user_tags ut on user.id = ut.user_id")
                .INNER_JOIN("tags t on t.id = ut.tag_id");
        mergeSearchConditionToSql(sql, param, false);
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("user.username", GXBaseBuilderConstant.RIGHT_LIKE)
                .set("t.name", GXBaseBuilderConstant.STR_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return "user";
    }
}
