package cn.maple.core.datasource.util;

import cn.hutool.core.thread.ThreadUtil;
import cn.maple.core.datasource.dto.GXDataFilterInnerDto;

public class GXDataFilterThreadLocalUtils {
    private static final ThreadLocal<GXDataFilterInnerDto> DATA_FILTER_INNER_DTO = ThreadUtil.createThreadLocal(true);

    private GXDataFilterThreadLocalUtils() {

    }

    public static GXDataFilterInnerDto getDataFilterInnerDto() {
        return DATA_FILTER_INNER_DTO.get();
    }

    public static void setDataFilterInnerDto(GXDataFilterInnerDto dto) {
        DATA_FILTER_INNER_DTO.set(dto);
    }

    public static void cleanDataFilterInnerDto() {
        DATA_FILTER_INNER_DTO.remove();
    }
}
