package cn.maple.sso.enums;

/**
 * <p>
 * Token 状态标记
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public enum GXTokenFlag {
    NORMAL(0, "正常"),

    CACHE_SHUT(1, "缓存宕机");

    /**
     * 主键
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    GXTokenFlag(final Integer value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static GXTokenFlag fromValue(String value) {
        GXTokenFlag[] its = GXTokenFlag.values();
        for (GXTokenFlag it : its) {
            if (it.value().equals(value)) {
                return it;
            }
        }
        return NORMAL;
    }

    public Integer value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }
}
