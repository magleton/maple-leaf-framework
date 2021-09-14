package com.geoxus.ueditor.upload.impl;

import com.geoxus.ueditor.GXPathFormat;
import com.geoxus.ueditor.GXStorageManager;
import com.geoxus.ueditor.config.GXEditorProperties;
import com.geoxus.ueditor.define.GXBaseState;
import com.geoxus.ueditor.define.GXEditorResponseInfo;
import com.geoxus.ueditor.define.GXFileType;
import com.geoxus.ueditor.define.GXState;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GXBinaryUploader {
    @Autowired
    private GXEditorProperties properties;

    public GXState save(HttpServletRequest request, Map<String, Object> conf) {
        boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;
        if (!ServletFileUpload.isMultipartContent(request)) {
            return new GXBaseState(false, GXEditorResponseInfo.NOT_MULTIPART_CONTENT);
        }
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        if (isAjaxUpload) {
            upload.setHeaderEncoding("UTF-8");
        }
        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Collection<MultipartFile> files = mRequest.getFileMap().values();
            // 没有上传数据
            if (files.isEmpty()) {
                return new GXBaseState(false, GXEditorResponseInfo.NOTFOUND_UPLOAD_DATA);
            }
            // 获得文件
            MultipartFile file = files.iterator().next();
            // 文件名
            String originFileName = file.getOriginalFilename();
            // 文件扩展名
            assert originFileName != null;
            String suffix = GXFileType.getSuffixByFilename(originFileName);
            // 不符合文件类型
            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new GXBaseState(false, GXEditorResponseInfo.NOT_ALLOW_FILE_TYPE);
            }
            long maxSize = (Long) conf.get("maxSize");
            // 文件大小超出限制
            if (maxSize < file.getSize()) {
                return new GXBaseState(false, GXEditorResponseInfo.MAX_SIZE);
            }
            String savePath = (String) conf.get("savePath");
            savePath = savePath + suffix;
            savePath = GXPathFormat.parse(savePath, originFileName);
            String physicalPath = GXPathFormat.format(properties.getLocal().getPhysicalPath() + "/" + savePath);
            GXState storageState = GXStorageManager.saveFileByInputStream(file.getInputStream(), physicalPath);
            if (storageState.isSuccess()) {
                storageState.putInfo("url", GXPathFormat.format(conf.get("contextPath") + "/" + properties.getLocal().getUrlPrefix() + "/" + GXPathFormat.format(savePath)));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName);
            }
            return storageState;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new GXBaseState(false, GXEditorResponseInfo.IO_ERROR);
    }

    private boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);
        return list.contains(type);
    }
}
