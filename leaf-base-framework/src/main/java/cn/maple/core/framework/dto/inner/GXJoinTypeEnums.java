package cn.maple.core.framework.dto.inner;

import cn.maple.core.framework.constant.GXBuilderConstant;

public enum GXJoinTypeEnums {
    LEFT(GXBuilderConstant.LEFT_JOIN_TYPE, "左链接"),
    RIGHT(GXBuilderConstant.RIGHT_JOIN_TYPE, "右链接"),
    INNER(GXBuilderConstant.INNER_JOIN_TYPE, "内链接");

    private final String joinType;
    private final String desc;

    GXJoinTypeEnums(String joinType, String desc) {
        this.joinType = joinType;
        this.desc = desc;
    }

    public String getJoinType() {
        return joinType;
    }
}
