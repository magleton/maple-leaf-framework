package com.geoxus.ueditor.define;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 定义请求action类型
 */
@SuppressWarnings("serial")
public final class GXActionMap {
    protected static final Map<String, Integer> MAPPING = new HashMap<>();

    // 获取配置请求
    public static final int CONFIG = 0;
    public static final int UPLOAD_IMAGE = 1;
    public static final int UPLOAD_SCRAWL = 2;
    public static final int UPLOAD_VIDEO = 3;
    public static final int UPLOAD_FILE = 4;
    public static final int CATCH_IMAGE = 5;
    public static final int LIST_FILE = 6;
    public static final int LIST_IMAGE = 7;

    static {
        MAPPING.put("config", GXActionMap.CONFIG);
        MAPPING.put("uploadimage", GXActionMap.UPLOAD_IMAGE);
        MAPPING.put("uploadscrawl", GXActionMap.UPLOAD_SCRAWL);
        MAPPING.put("uploadvideo", GXActionMap.UPLOAD_VIDEO);
        MAPPING.put("uploadfile", GXActionMap.UPLOAD_FILE);
        MAPPING.put("catchimage", GXActionMap.CATCH_IMAGE);
        MAPPING.put("listfile", GXActionMap.LIST_FILE);
        MAPPING.put("listimage", GXActionMap.LIST_IMAGE);
    }

    private GXActionMap() {
    }

    public static int getType(String key) {
        return Optional.ofNullable(GXActionMap.MAPPING.get(key)).orElse(-1);
    }
}
