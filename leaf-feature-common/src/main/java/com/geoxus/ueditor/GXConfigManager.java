package com.geoxus.ueditor;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.geoxus.ueditor.define.GXActionMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理器
 */
final class GXConfigManager {
    // 涂鸦上传filename定义
    private static final String SCRAWL_FILE_NAME = "scrawl";
    // 远程图片抓取filename定义
    private static final String REMOTE_FILE_NAME = "remote";
    private final String configFile;
    private JSONObject jsonConfig = null;

    /**
     * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
     */
    private GXConfigManager(String configFile) throws IOException {
        this.configFile = configFile;
        this.initEnv();
    }

    /**
     * 配置管理器构造工厂
     *
     * @param configFile 配置文件
     * @return 配置管理器实例或者null
     */
    static GXConfigManager getInstance(String configFile) {
        try {
            return new GXConfigManager(configFile);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证配置文件加载是否正确
     */
    boolean valid() {
        return this.jsonConfig != null;
    }

    JSONObject getAllConfig() {
        return this.jsonConfig;
    }

    Map<String, Object> getConfig(int type) throws JSONException {
        Map<String, Object> conf = new HashMap<>();
        String savePath = null;
        switch (type) {
            case GXActionMap.UPLOAD_FILE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.getLong("fileMaxSize"));
                conf.put("allowFiles", this.getArray("fileAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getStr("fileFieldName"));
                savePath = this.jsonConfig.getStr("filePathFormat");
                break;

            case GXActionMap.UPLOAD_IMAGE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
                conf.put("allowFiles", this.getArray("imageAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getStr("imageFieldName"));
                savePath = this.jsonConfig.getStr("imagePathFormat");
                break;

            case GXActionMap.UPLOAD_VIDEO:
                conf.put("maxSize", this.jsonConfig.getLong("videoMaxSize"));
                conf.put("allowFiles", this.getArray("videoAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getStr("videoFieldName"));
                savePath = this.jsonConfig.getStr("videoPathFormat");
                break;

            case GXActionMap.UPLOAD_SCRAWL:
                conf.put("filename", GXConfigManager.SCRAWL_FILE_NAME);
                conf.put("maxSize", this.jsonConfig.getLong("scrawlMaxSize"));
                conf.put("fieldName", this.jsonConfig.getStr("scrawlFieldName"));
                conf.put("isBase64", "true");
                savePath = this.jsonConfig.getStr("scrawlPathFormat");
                break;

            case GXActionMap.CATCH_IMAGE:
                conf.put("filename", GXConfigManager.REMOTE_FILE_NAME);
                conf.put("filter", this.getArray("catcherLocalDomain"));
                conf.put("maxSize", this.jsonConfig.getLong("catcherMaxSize"));
                conf.put("allowFiles", this.getArray("catcherAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getStr("catcherFieldName") + "[]");
                savePath = this.jsonConfig.getStr("catcherPathFormat");
                break;

            case GXActionMap.LIST_IMAGE:
                conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.getStr("imageManagerListPath"));
                conf.put("count", this.jsonConfig.getInt("imageManagerListSize"));
                break;

            case GXActionMap.LIST_FILE:
                conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.getStr("fileManagerListPath"));
                conf.put("count", this.jsonConfig.getInt("fileManagerListSize"));
                break;
        }
        conf.put("savePath", savePath);
        return conf;
    }

    private void initEnv() throws IOException {
        String configContent = this.readFile(this.configFile);
        try {
            this.jsonConfig = new JSONObject(configContent);
        } catch (Exception e) {
            this.jsonConfig = null;
        }
    }

    private String[] getArray(String key) throws JSONException {
        JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
        String[] result = new String[jsonArray.size()];
        for (int i = 0, len = jsonArray.size(); i < len; i++) {
            result[i] = jsonArray.getStr(i);
        }
        return result;
    }

    /**
     * 读取配置文件
     *
     * @param path resources目录下配置文件的位置 如（static/ueditor/config.json）
     */
    private String readFile(String path) throws IOException {
        StringBuilder builder = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(path), StandardCharsets.UTF_8);
            BufferedReader bfReader = new BufferedReader(reader);
            String tmpContent = null;
            while ((tmpContent = bfReader.readLine()) != null) {
                builder.append(tmpContent);
            }
            bfReader.close();
        } catch (UnsupportedEncodingException e) {
            // 忽略
        }
        return this.filter(builder.toString());
    }

    /**
     * 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
     */
    private String filter(String input) {
        return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
    }
}
