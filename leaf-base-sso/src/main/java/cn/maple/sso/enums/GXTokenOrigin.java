package cn.maple.sso.enums;

/**
 * <p>
 * Token 登录授权来源
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public enum GXTokenOrigin {
    COOKIE("0", "cookie"),
    HTML5("1", "html5"),
    IOS("2", "apple ios"),
    ANDROID("3", "google android");

    /**
     * 主键
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    GXTokenOrigin(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static GXTokenOrigin fromValue(String value) {
        GXTokenOrigin[] its = GXTokenOrigin.values();
        for (GXTokenOrigin it : its) {
            if (it.value().equals(value)) {
                return it;
            }
        }
        return COOKIE;
    }

    public String value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }
}
