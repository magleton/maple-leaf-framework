package com.geoxus.ueditor.define;

import java.util.HashMap;
import java.util.Map;

public class GXMIMEType {

    private GXMIMEType() {
    }

    private static final Map<String, String> TYPES = new HashMap<>();

    static {
        TYPES.put("image/gif", ".gif");
        TYPES.put("image/jpeg", ".jpg");
        TYPES.put("image/jpg", ".jpg");
        TYPES.put("image/png", ".png");
        TYPES.put("image/bmp", ".bmp");
    }

    public static String getSuffix(String mime) {
        return GXMIMEType.TYPES.get(mime);
    }
}
