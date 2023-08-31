package cn.maple.core.framework.dto.inner;

public enum GXUnionTypeEnums {
    UNION_ALL("union all", "UNION ALL"),
    UNION("union", "UNION");
    private final String unionType;
    private final String desc;

    GXUnionTypeEnums(String unionType, String desc) {
        this.unionType = unionType;
        this.desc = desc;
    }

    public String getUnionType() {
        return unionType;
    }
}
