package cn.maple.sso.enums;

/**
 * <p>
 * SSO 执行状态
 * </p>
 *
 * @author britton
 * @since 2021-09-15
 */
public enum GXAction {
    /**
     * 正常（默认）
     */
    Normal("0", "执行权限验证"),

    /**
     * 跳过
     */
    Skip("1", "跳过权限验证");

    /**
     * 主键
     */
    private final String key;

    /**
     * 描述
     */
    private final String desc;

    GXAction(final String key, final String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

}
