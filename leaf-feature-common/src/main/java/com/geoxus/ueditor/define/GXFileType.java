package com.geoxus.ueditor.define;

import java.util.HashMap;
import java.util.Map;

public class GXFileType {

    private static final String JPG = "JPG";

    private GXFileType() {
    }

    private static final Map<String, String> TYPES = new HashMap<>();

    static {
        TYPES.put(GXFileType.JPG, ".jpg");
    }

    public static String getSuffix(String key) {
        return GXFileType.TYPES.get(key);
    }

    /**
     * 根据给定的文件名,获取其后缀信息
     */
    public static String getSuffixByFilename(String filename) {
        return filename.substring(filename.lastIndexOf('.')).toLowerCase();
    }
}
