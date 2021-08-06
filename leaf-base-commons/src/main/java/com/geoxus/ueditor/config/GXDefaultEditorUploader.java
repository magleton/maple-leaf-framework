package com.geoxus.ueditor.config;

import com.geoxus.ueditor.define.GXState;
import com.geoxus.ueditor.hunter.GXFileManager;
import com.geoxus.ueditor.hunter.GXImageHunter;
import com.geoxus.ueditor.upload.GXEditorUploader;
import com.geoxus.ueditor.upload.impl.GXBase64Uploader;
import com.geoxus.ueditor.upload.impl.GXBinaryUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class GXDefaultEditorUploader implements GXEditorUploader {
    @Autowired
    private GXBinaryUploader binaryUploader;

    @Override
    public GXState binaryUpload(HttpServletRequest request, Map<String, Object> conf) {
        return binaryUploader.save(request, conf);
    }

    @Override
    public GXState base64Upload(HttpServletRequest request, Map<String, Object> conf) {
        String filedName = (String) conf.get("fieldName");
        return GXBase64Uploader.save(request.getParameter(filedName), conf);
    }

    @Override
    public GXState listImage(int index, Map<String, Object> conf) {
        return new GXFileManager(conf).listFile(index);
    }

    @Override
    public GXState listFile(int index, Map<String, Object> conf) {
        return new GXFileManager(conf).listFile(index);
    }

    @Override
    public GXState imageHunter(String[] list, Map<String, Object> conf) {
        return new GXImageHunter(conf).capture(list);
    }
}