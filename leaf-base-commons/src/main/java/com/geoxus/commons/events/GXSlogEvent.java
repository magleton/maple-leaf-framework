package com.geoxus.commons.events;

import cn.hutool.core.lang.Dict;
import lombok.Data;

@Data
public class GXSlogEvent<T> {
    private String source;
    private T target;
    private Dict param;
    private String tableName;

    public GXSlogEvent(String source, T target, String tableName, Dict param) {
        this.source = source;
        this.target = target;
        this.tableName = tableName;
        this.param = param;
    }
}
