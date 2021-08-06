package com.geoxus.ueditor;

import cn.hutool.json.JSONException;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.ueditor.config.GXDefaultEditorUploader;
import com.geoxus.ueditor.config.GXEditorProperties;
import com.geoxus.ueditor.define.GXActionMap;
import com.geoxus.ueditor.define.GXBaseState;
import com.geoxus.ueditor.define.GXEditorResponseInfo;
import com.geoxus.ueditor.define.GXState;
import com.geoxus.ueditor.upload.GXEditorUploader;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class GXActionEnter {
    private final GXEditorUploader editorUploader;

    private final GXEditorProperties properties = GXSpringContextUtils.getBean(GXEditorProperties.class);

    private final HttpServletRequest request;

    private final String actionType;

    private final GXConfigManager configManager;

    public GXActionEnter(HttpServletRequest request) {
        this.request = request;
        this.actionType = request.getParameter("action");
        this.configManager = GXConfigManager.getInstance(properties.getConfigFile());
        editorUploader = GXSpringContextUtils.getBean(GXDefaultEditorUploader.class);
    }

    public String exec() throws JSONException {
        String callbackName = this.request.getParameter("callback");
        if (callbackName != null) {
            if (!validCallbackName(callbackName)) {
                return new GXBaseState(false, GXEditorResponseInfo.ILLEGAL).toJSONString();
            }
            return callbackName + "(" + this.invoke() + ");";
        } else {
            return this.invoke();
        }
    }

    private String invoke() throws JSONException {
        if (actionType == null || -1 == GXActionMap.getType(actionType)) {
            return new GXBaseState(false, GXEditorResponseInfo.INVALID_ACTION).toJSONString();
        }
        if (this.configManager == null || !this.configManager.valid()) {
            return new GXBaseState(false, GXEditorResponseInfo.CONFIG_ERROR).toJSONString();
        }
        GXState state = null;
        int actionCode = GXActionMap.getType(this.actionType);
        Map<String, Object> conf;
        final String contextPath = "contextPath";
        switch (actionCode) {
            case GXActionMap.CONFIG:
                return this.configManager.getAllConfig().toString();
            case GXActionMap.UPLOAD_IMAGE:
            case GXActionMap.UPLOAD_SCRAWL:
            case GXActionMap.UPLOAD_VIDEO:
            case GXActionMap.UPLOAD_FILE:
                conf = this.configManager.getConfig(actionCode);
                conf.put(contextPath, request.getContextPath());
                System.out.println("conf" + conf);
                if ("true".equals(conf.get("isBase64"))) {
                    state = editorUploader.base64Upload(request, conf);
                } else {
                    state = editorUploader.binaryUpload(request, conf);
                }
                break;
            case GXActionMap.CATCH_IMAGE:
                conf = configManager.getConfig(actionCode);
                conf.put(contextPath, request.getContextPath());
                String[] list = this.request.getParameterValues((String) conf.get("fieldName"));
                state = editorUploader.imageHunter(list, conf);
                break;
            case GXActionMap.LIST_IMAGE:
                conf = configManager.getConfig(actionCode);
                conf.put(contextPath, request.getContextPath());
                state = editorUploader.listImage(this.getStartIndex(), conf);
                break;
            case GXActionMap.LIST_FILE:
                conf = configManager.getConfig(actionCode);
                conf.put(contextPath, request.getContextPath());
                state = editorUploader.listFile(this.getStartIndex(), conf);
                break;
            default:
                break;
        }
        assert state != null;
        return state.toJSONString();
    }

    private int getStartIndex() {
        String start = this.request.getParameter("start");
        try {
            return Integer.parseInt(start);
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean validCallbackName(String name) {
        return name.matches("^[a-zA-Z_]+[\\w0-9_]*$");
    }
}
