package cn.maple.core.framework.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.inner.permission.GXBasePermissionInnerDto;

import java.util.List;
import java.util.Map;

public class GXPermissionEvent extends GXBaseEvent<Map<String, List<GXBasePermissionInnerDto>>> {
    public GXPermissionEvent(Map<String, List<GXBasePermissionInnerDto>> source, Dict param) {
        this(source, "", param, "");
    }

    public GXPermissionEvent(Map<String, List<GXBasePermissionInnerDto>> source, String eventName, Dict param) {
        this(source, eventName, param, "");
    }

    public GXPermissionEvent(Map<String, List<GXBasePermissionInnerDto>> source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }
}
