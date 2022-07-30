package cn.maple.core.datasource.util;

import cn.hutool.core.thread.ThreadUtil;
import cn.maple.core.datasource.dto.GXDataFilterInnerDto;

public class GXThreadLocalUtils {
    private static final ThreadLocal<GXDataFilterInnerDto> DATA_FILTER_THREAD_LOCAL = ThreadUtil.createThreadLocal(true);

    private GXThreadLocalUtils() {

    }

    public static ThreadLocal<GXDataFilterInnerDto> getDataFilterThreadLocal() {
        return DATA_FILTER_THREAD_LOCAL;
    }
}
