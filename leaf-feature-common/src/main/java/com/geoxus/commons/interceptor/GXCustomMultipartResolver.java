package com.geoxus.commons.interceptor;

import com.geoxus.commons.vo.GXUploadProgressListener;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GXCustomMultipartResolver extends CommonsMultipartResolver {
    @Autowired
    private GXUploadProgressListener uploadProgressListener;

    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) {
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        //单个文件大小MB
        fileUpload.setFileSizeMax(1024L * 1024 * 500);
        //一次提交文件大小MB
        fileUpload.setSizeMax(1024L * 1024 * 500);
        //文件上传进度监听器设置session用于存储上传进度
        uploadProgressListener.setSession(request.getSession());
        //将文件上传进度监听器加入到 fileUpload 中
        fileUpload.setProgressListener(uploadProgressListener);
        logger.info(fileUpload.getProgressListener());
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return parseFileItems(fileItems, encoding);
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadBase.FileSizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getFileSizeMax(), ex);
        } catch (FileUploadException ex) {
            throw new MultipartException("Failed to parse multipart servlet request", ex);
        }
    }
}
