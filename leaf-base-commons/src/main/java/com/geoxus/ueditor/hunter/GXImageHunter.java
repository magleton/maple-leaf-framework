package com.geoxus.ueditor.hunter;


import com.geoxus.ueditor.GXPathFormat;
import com.geoxus.ueditor.GXStorageManager;
import com.geoxus.ueditor.config.GXEditorProperties;
import com.geoxus.ueditor.define.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 图片抓取器
 */
public class GXImageHunter {
    @Autowired
    private GXEditorProperties properties;

    private String filename = null;
    private String savePath = null;
    private String contextPath = null;
    private List<String> allowTypes = null;
    private long maxSize = -1;

    private List<String> filters = null;

    public GXImageHunter(Map<String, Object> conf) {
        this.filename = (String) conf.get("filename");
        this.savePath = (String) conf.get("savePath");
        this.contextPath = (String) conf.get("contextPath");
        this.maxSize = (Long) conf.get("maxSize");
        this.allowTypes = Arrays.asList((String[]) conf.get("allowFiles"));
        this.filters = Arrays.asList((String[]) conf.get("filter"));

    }

    public GXState capture(String[] list) {
        GXMultiState state = new GXMultiState(true);
        for (String source : list) {
            state.addState(captureRemoteData(source));
        }
        return state;
    }

    public GXState captureRemoteData(String urlStr) {
        HttpURLConnection connection = null;
        URL url = null;
        String suffix = null;
        try {
            url = new URL(urlStr);
            if (!validHost(url.getHost())) {
                return new GXBaseState(false, GXEditorResponseInfo.PREVENT_HOST);
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(true);
            if (!validContentState(connection.getResponseCode())) {
                return new GXBaseState(false, GXEditorResponseInfo.CONNECTION_ERROR);
            }
            suffix = GXMIMEType.getSuffix(connection.getContentType());
            if (!validFileType(suffix)) {
                return new GXBaseState(false, GXEditorResponseInfo.NOT_ALLOW_FILE_TYPE);
            }
            if (!validFileSize(connection.getContentLength())) {
                return new GXBaseState(false, GXEditorResponseInfo.MAX_SIZE);
            }
            String savePath = GXPathFormat.parse(this.savePath, (String) this.filename);
            savePath = savePath + suffix;
            String physicalPath = GXPathFormat.format(properties.getLocal().getPhysicalPath() + "/" + savePath);
            GXState state = GXStorageManager.saveFileByInputStream(connection.getInputStream(), physicalPath);
            if (state.isSuccess()) {
                state.putInfo("url", GXPathFormat.format(this.contextPath + "/" + properties.getLocal().getUrlPrefix() + savePath));
                state.putInfo("source", urlStr);
            }
            return state;
        } catch (Exception e) {
            return new GXBaseState(false, GXEditorResponseInfo.REMOTE_FAIL);
        }
    }

    private boolean validHost(String hostname) {
        try {
            InetAddress ip = InetAddress.getByName(hostname);

            if (ip.isSiteLocalAddress()) {
                return false;
            }
        } catch (UnknownHostException e) {
            return false;
        }

        return !filters.contains(hostname);
    }

    private boolean validContentState(int code) {
        return HttpURLConnection.HTTP_OK == code;
    }

    private boolean validFileType(String type) {
        return this.allowTypes.contains(type);
    }

    private boolean validFileSize(int size) {
        return size < this.maxSize;
    }
}
