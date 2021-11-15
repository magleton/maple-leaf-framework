package cn.maple.core.framework.exception;

import java.sql.SQLException;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:10
 * <p>
 * 自定义SQL异常
 */
public class GXDBSQLException extends SQLException {
    public GXDBSQLException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }
}
