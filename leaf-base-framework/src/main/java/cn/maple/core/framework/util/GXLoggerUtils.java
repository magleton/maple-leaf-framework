package cn.maple.core.framework.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
     * @param desc   格式
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logInfo(Logger logger, String desc, Object... data) {
        if (data.length == 0) {
            return;
        }
        String format = generateFormat(desc, data);
        logger.info(format, data);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param desc   格式
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logDebug(Logger logger, String desc, Object... data) {
        if (data.length == 0) {
            return;
        }
        String format = generateFormat(desc, data);
        logger.debug(format, data);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param desc   格式
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logError(Logger logger, String desc, Object... data) {
        if (data.length == 0) {
            return;
        }
        String format = generateFormat(desc, data);
        logger.error(format, data);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param desc   日志格式描述
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logWarn(Logger logger, String desc, Object... data) {
        if (data.length == 0) {
            return;
        }
        String format = generateFormat(desc, data);
        logger.warn(format, data);
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
     * 生成日志的格式化字符串
     *
     * @param desc 日志描述信息
     * @param data 需要记录的数据
     * @return 日志格式化字符串
     */
    private static String generateFormat(String desc, Object... data) {
        String threadName = Thread.currentThread().getName();
        String format = CharSequenceUtil.format(GXCommonConstant.SHORT_LOGGER_FORMAT, threadName, desc);
        int length = data.length;
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            strings.add("{}");
        }
        String appendFormat = Strings.join(strings, ',');
        return format + " --> 日志详细 : " + appendFormat;
    }
}
