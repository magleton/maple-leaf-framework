package cn.maple.core.framework.util;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import org.slf4j.Logger;

import java.util.Optional;

@SuppressWarnings("unused")
public class GXLoggerUtils {
    /**
     * 私有构造函数
     */
    private GXLoggerUtils() {
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
    public static void logInfo(Logger logger, String desc, Object data) {
        String threadName = Thread.currentThread().getName();
        String jsonStr = JSONUtil.toJsonStr(addTraceIdToData(data));
        String s = CharSequenceUtil.format(GXCommonConstant.LOGGER_FORMAT, threadName, desc, jsonStr);
        logger.info(s);
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
    public static void logDebug(Logger logger, String desc, Object data) {
        String threadName = Thread.currentThread().getName();
        String jsonStr = JSONUtil.toJsonStr(addTraceIdToData(data));
        String format = CharSequenceUtil.format(GXCommonConstant.LOGGER_FORMAT, threadName, desc, jsonStr);
        logger.debug(format);
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
    public static void logError(Logger logger, String desc, Object data) {
        String threadName = Thread.currentThread().getName();
        String jsonStr = JSONUtil.toJsonStr(addTraceIdToData(data));
        String format = CharSequenceUtil.format(GXCommonConstant.LOGGER_FORMAT, threadName, desc, jsonStr);
        logger.error(format);
    }

    /**
     * 记录日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param t      异常信息
     * @author britton
     * @since 2021-10-19 17:40
     */
    public static void logError(Logger logger, String desc, Throwable t) {
        String threadName = Thread.currentThread().getName();
        JSONObject jsonObject = addTraceIdToData(Dict.create().set(threadName, desc));
        String format = JSONUtil.toJsonStr(jsonObject);
        logger.error(format, t);
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
    public static void logWarn(Logger logger, String desc, Object data) {
        String threadName = Thread.currentThread().getName();
        String jsonStr = JSONUtil.toJsonStr(addTraceIdToData(data));
        String s = CharSequenceUtil.format(GXCommonConstant.LOGGER_FORMAT, threadName, desc, jsonStr);
        logger.warn(s);
    }

    /**
     * 添加额外数据到日志数据对象
     *
     * @param data 需要记录的数据对象
     * @return JSONObject
     */
    private static JSONObject addTraceIdToData(Object data) {
        data = Optional.ofNullable(data).orElse(Dict.create());
        if (CharSequenceUtil.equalsIgnoreCase(GXTypeOfUtils.typeof(data).getName(), String.class.getName())) {
            data = Dict.create().set("message", data);
        }
        JSONObject parse = JSONUtil.parseObj(data);
        String traceId = GXTraceIdContextUtils.getTraceId();
        parse.putByPath(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
        return parse;
    }
}
