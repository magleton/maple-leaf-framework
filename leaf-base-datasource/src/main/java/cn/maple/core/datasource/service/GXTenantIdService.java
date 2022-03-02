package cn.maple.core.datasource.service;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 获取租户ID
 */
public interface GXTenantIdService {
    /**
     * 获取租户ID
     *
     * @return Expression
     */
    default Expression getTenantId() {
        return new LongValue(1);
    }
}
