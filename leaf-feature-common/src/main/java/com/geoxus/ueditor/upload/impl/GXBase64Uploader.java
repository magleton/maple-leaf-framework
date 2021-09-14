package com.geoxus.ueditor.upload.impl;

import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.ueditor.GXPathFormat;
import com.geoxus.ueditor.GXStorageManager;
import com.geoxus.ueditor.config.GXEditorProperties;
import com.geoxus.ueditor.define.GXBaseState;
import com.geoxus.ueditor.define.GXEditorResponseInfo;
import com.geoxus.ueditor.define.GXFileType;
import com.geoxus.ueditor.define.GXState;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Map;

public class GXBase64Uploader {
    private static final GXEditorProperties properties;

    static {
        properties = GXSpringContextUtils.getBean(GXEditorProperties.class);
    }

    private GXBase64Uploader() {
    }

    public static GXState save(String content, Map<String, Object> conf) {
        byte[] data = decode(content);
        long maxSize = (Long) conf.get("maxSize");
        if (!validSize(data, maxSize)) {
            return new GXBaseState(false, GXEditorResponseInfo.MAX_SIZE);
        }
        String suffix = GXFileType.getSuffix("JPG");
        String savePath = GXPathFormat.parse((String) conf.get("savePath"), (String) conf.get("filename"));
        savePath = savePath + suffix;
        String physicalPath = GXPathFormat.format(properties.getLocal().getPhysicalPath() + "/" + savePath);
        GXState storageState = GXStorageManager.saveBinaryFile(data, physicalPath);
        if (storageState.isSuccess()) {
            storageState.putInfo("url", GXPathFormat.format(conf.get("contextPath") + "/" + properties.getLocal().getUrlPrefix() + savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }
        return storageState;
    }

    private static byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }
}
