package cn.maple.core.framework.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.service.GXLoggerService;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@ConditionalOnMissingClass(value = "org.apache.skywalking.apm.toolkit.trace.ActiveSpan")
public class GXLocalLoggerServiceImpl implements GXLoggerService {
    /**
     * 记录Info日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    @Override
    public void logInfo(Logger logger, String desc, Object data) {
        String threadName = Thread.currentThread().getName();
        String jsonStr = "";
        if (Objects.nonNull(data)) {
            jsonStr = JSONUtil.toJsonStr(data);
        }
        String s = CharSequenceUtil.format(GXCommonConstant.LOGGER_FORMAT, threadName, desc, jsonStr);
        logger.info(s);
    }

    /**
     * 记录Debug日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    @Override
    public void logDebug(Logger logger, String desc, Object data) {
        String threadName = Thread.currentThread().getName();
        String jsonStr = "";
        if (Objects.nonNull(data)) {
            jsonStr = JSONUtil.toJsonStr(data);
        }
        String format = CharSequenceUtil.format(GXCommonConstant.LOGGER_FORMAT, threadName, desc, jsonStr);
        logger.debug(format);
    }

    /**
     * 记录Error日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    @Override
    public void logError(Logger logger, String desc, Object data) {
        String threadName = Thread.currentThread().getName();
        String jsonStr = "";
        if (Objects.nonNull(data)) {
            jsonStr = JSONUtil.toJsonStr(data);
        }
        String format = CharSequenceUtil.format(GXCommonConstant.LOGGER_FORMAT, threadName, desc, jsonStr);
        logger.error(format);
    }

    /**
     * 记录Error日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param t      异常信息
     * @author britton
     * @since 2021-10-19 17:40
     */
    @Override
    public void logError(Logger logger, String desc, Throwable t) {
        String threadName = Thread.currentThread().getName();
        String format = CharSequenceUtil.format("{} : {}", threadName, desc);
        logger.error(format, t);
    }

    /**
     * 记录Warning日志
     *
     * @param logger 日志对象
     * @param desc   描述信息
     * @param data   记录数据
     * @author britton
     * @since 2021-10-19 17:40
     */
    @Override
    public void logWarn(Logger logger, String desc, Object data) {
        String threadName = Thread.currentThread().getName();
        String jsonStr = "";
        if (Objects.nonNull(data)) {
            jsonStr = JSONUtil.toJsonStr(data);
        }
        String s = CharSequenceUtil.format(GXCommonConstant.LOGGER_FORMAT, threadName, desc, jsonStr);
        logger.warn(s);
    }
}
