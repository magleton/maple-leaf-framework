package cn.maple.core.framework.exception;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:06
 * <p>
 * 数据库通用异常
 */
public class GXDBException extends Exception {
    /**
     * code错误码
     */
    private final Integer code;

    public GXDBException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
