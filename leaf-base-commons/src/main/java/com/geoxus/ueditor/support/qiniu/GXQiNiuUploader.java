package com.geoxus.ueditor.support.qiniu;

import com.geoxus.ueditor.GXPathFormat;
import com.geoxus.ueditor.config.GXEditorProperties;
import com.geoxus.ueditor.define.*;
import com.geoxus.ueditor.upload.GXEditorUploader;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 七牛云上传实现
 */
@Component
public class GXQiNiuUploader implements GXEditorUploader {
    @Autowired
    private GXEditorProperties properties;

    @Override
    public GXState binaryUpload(HttpServletRequest request, Map<String, Object> conf) {
        String fieldName = (String) conf.get("fieldName");
        MultiValueMap<String, MultipartFile> multiFileMap = ((MultipartHttpServletRequest) request).getMultiFileMap();
        MultipartFile file = multiFileMap.getFirst(fieldName);
        assert file != null;
        // 文件名
        String originFileName = file.getOriginalFilename();
        assert originFileName != null;
        // 文件扩展名
        String suffix = originFileName.substring(originFileName.lastIndexOf(".")).toLowerCase();
        // 不符合文件类型
        if (!Arrays.asList((String[]) conf.get("allowFiles")).contains(suffix)) {
            return new GXBaseState(false, GXEditorResponseInfo.NOT_ALLOW_FILE_TYPE);
        }
        long maxSize = (Long) conf.get("maxSize");
        // 文件大小超出限制
        if (maxSize < file.getSize()) {
            return new GXBaseState(false, GXEditorResponseInfo.MAX_SIZE);
        }
        // 根据config.json 中的 imagePathFormat 生成 路径+文件名
        String savePath = (String) conf.get("savePath");
        savePath = savePath + suffix;
        savePath = GXPathFormat.parse(savePath, originFileName);
        String url = GXQiNiuUtils.upload(file, savePath);
        GXBaseState baseState = new GXBaseState();
        // 必填项url，图片地址
        baseState.putInfo("url", url);
        return baseState;
    }

    @Override
    public GXState base64Upload(HttpServletRequest request, Map<String, Object> conf) {
        String filedName = (String) conf.get("fieldName");
        String content = request.getParameter(filedName);
        byte[] data = Base64.decodeBase64(content);
        long maxSize = (Long) conf.get("maxSize");
        // 文件大小超出限制
        if (data.length > maxSize) {
            return new GXBaseState(false, GXEditorResponseInfo.MAX_SIZE);
        }
        // 根据config.json 中的 imagePathFormat 生成 路径+文件名
        String savePath = GXPathFormat.parse((String) conf.get("savePath"), (String) conf.get("filename"));
        savePath = savePath + ".jpg";
        String url = GXQiNiuUtils.upload(content, savePath);
        GXBaseState baseState = new GXBaseState();
        // 必填项url，图片地址
        baseState.putInfo("url", url);
        return baseState;
    }


    @Override
    public GXMultiState listImage(int index, Map<String, Object> conf) {
        return this.listFile(index, conf);
    }

    @Override
    public GXMultiState listFile(int index, Map<String, Object> conf) {
        // 每次列出图片数量 config.json 中的 imageManagerListSize
        int count = (Integer) conf.get("count");
        String dir = (String) conf.get("dir");
        GXTotal total = new GXTotal();
        List<String> listFile = GXQiNiuUtils.listFile(dir, index, count, total);
        GXMultiState state = null;
        if (listFile == null) {
            state = new GXMultiState(true);
        } else {
            state = new GXMultiState(true);
            GXBaseState fileState = null;
            for (String key : listFile) {
                fileState = new GXBaseState(true);
                fileState.putInfo("url", key);
                state.addState(fileState);

            }
        }
        state.putInfo("start", index);
        state.putInfo("total", total.getTotal());
        return state;
    }

    @Override
    public GXState imageHunter(String[] list, Map<String, Object> conf) {
        List<String> allowTypes = Arrays.asList((String[]) conf.get("allowFiles"));
        Long maxSize = (Long) conf.get("maxSize");
        String filename = (String) conf.get("filename");
        String savePath = (String) conf.get("savePath");
        List<String> filters = Arrays.asList((String[]) conf.get("filter"));
        GXMultiState state = new GXMultiState(true);
        for (String source : list) {
            state.addState(saveRemoteImage(source, allowTypes, filters, maxSize, filename, savePath));
        }
        return state;
    }

    private GXState saveRemoteImage(String urlStr, List<String> allowTypes, List<String> filters, Long maxSize, String filename, String savePath) {
        HttpURLConnection connection = null;
        URL url = null;
        String suffix = null;
        try {
            url = new URL(urlStr);
            if (!validHost(url.getHost(), filters)) {
                return new GXBaseState(false, GXEditorResponseInfo.PREVENT_HOST);
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(true);
            if (!validContentState(connection.getResponseCode())) {
                return new GXBaseState(false, GXEditorResponseInfo.CONNECTION_ERROR);
            }
            suffix = GXMIMEType.getSuffix(connection.getContentType());
            if (!allowTypes.contains(suffix)) {
                return new GXBaseState(false, GXEditorResponseInfo.NOT_ALLOW_FILE_TYPE);
            }
            if (!(connection.getContentLength() < maxSize)) {
                return new GXBaseState(false, GXEditorResponseInfo.MAX_SIZE);
            }
            savePath = savePath + suffix;
            savePath = GXPathFormat.parse(savePath, filename);
            String qiniuUrl = GXQiNiuUtils.upload(connection.getInputStream(), savePath);
            GXBaseState baseState = new GXBaseState();
            baseState.putInfo("url", qiniuUrl);
            baseState.putInfo("source", urlStr);
            return baseState;
        } catch (Exception e) {
            return new GXBaseState(false, GXEditorResponseInfo.REMOTE_FAIL);
        }
    }

    private boolean validHost(String hostname, List<String> filters) {
        try {
            InetAddress ip = InetAddress.getByName(hostname);
            if (ip.isSiteLocalAddress()) {
                return false;
            }
        } catch (UnknownHostException e) {
            return false;
        }
        try {
            String cdn = properties.getQiniu().getCdn();
            URL url = new URL(cdn);
            if (url.getHost().equals(hostname)) {
                return false;
            }
        } catch (MalformedURLException ignore) {
        }
        return !filters.contains(hostname);
    }

    private boolean validContentState(int code) {
        return HttpURLConnection.HTTP_OK == code;
    }
}
