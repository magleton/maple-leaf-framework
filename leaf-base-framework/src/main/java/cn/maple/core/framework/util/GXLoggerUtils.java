package cn.maple.core.framework.util;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class GXLoggerUtils {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(GXLoggerUtils.class);

    private GXLoggerUtils() {
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param format 格式
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logInfo(Logger logger, String format, Object... data) {
        String threadName = Thread.currentThread().getName();
        format = CharSequenceUtil.format(GXCommonConstant.SHORT_LOGGER_FORMAT, threadName, format);
        logger.info(format, data);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logInfo(Logger logger, String desc, Dict data) {
        String threadName = Thread.currentThread().getName();
        if (Objects.nonNull(data)) {
            data = Dict.create();
        }
        String s = CharSequenceUtil.format(GXCommonConstant.FULL_LOGGER_FORMAT, threadName, desc, JSONUtil.toJsonStr(data));
        logger.info(s);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param format 格式
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logDebug(Logger logger, String format, Object... data) {
        String threadName = Thread.currentThread().getName();
        format = CharSequenceUtil.format(GXCommonConstant.SHORT_LOGGER_FORMAT, threadName, format);
        logger.debug(format, data);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logDebug(Logger logger, String desc, Dict data) {
        String threadName = Thread.currentThread().getName();
        if (Objects.nonNull(data)) {
            data = Dict.create();
        }
        String format = CharSequenceUtil.format(GXCommonConstant.FULL_LOGGER_FORMAT, threadName, desc, JSONUtil.toJsonStr(data));
        logger.debug(format);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param format 格式
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logError(Logger logger, String format, Object... data) {
        String threadName = Thread.currentThread().getName();
        format = CharSequenceUtil.format(GXCommonConstant.SHORT_LOGGER_FORMAT, threadName, format);
        logger.error(format, data);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logError(Logger logger, String desc, Dict data) {
        String threadName = Thread.currentThread().getName();
        if (Objects.nonNull(data)) {
            data = Dict.create();
        }
        String format = CharSequenceUtil.format(GXCommonConstant.FULL_LOGGER_FORMAT, threadName, desc, JSONUtil.toJsonStr(data));
        logger.error(format);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param t      异常信息
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logError(Logger logger, Throwable t) {
        String threadName = Thread.currentThread().getName();
        String format = CharSequenceUtil.format("线程 : {}", threadName);
        logger.error(format, t);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param format 日志格式描述
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logWarn(Logger logger, String format, Object... data) {
        String threadName = Thread.currentThread().getName();
        format = CharSequenceUtil.format(GXCommonConstant.SHORT_LOGGER_FORMAT, threadName, format);
        logger.warn(format, data);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logWarn(Logger logger, String desc, Dict data) {
        String threadName = Thread.currentThread().getName();
        if (Objects.nonNull(data)) {
            data = Dict.create();
        }
        String s = CharSequenceUtil.format(GXCommonConstant.FULL_LOGGER_FORMAT, threadName, desc, JSONUtil.toJsonStr(data));
        logger.warn(s);
    }
}
