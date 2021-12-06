package cn.maple.core.framework.service;

import org.slf4j.Logger;

public interface GXLoggerService {
    /**
     * 记录Info日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    void logInfo(Logger logger, String desc, Object data);

    /**
     * 记录Debug日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    void logDebug(Logger logger, String desc, Object data);

    /**
     * 记录Error日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    void logError(Logger logger, String desc, Object data);

    /**
     * 记录Error日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param t      异常信息
     * @author britton
     * @since 2021-10-19 17:40
     */
    void logError(Logger logger, String desc, Throwable t);

    /**
     * 记录Warning日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    void logWarn(Logger logger, String desc, Object data);
}
