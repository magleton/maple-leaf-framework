package com.geoxus.ueditor.define;

import java.util.HashMap;
import java.util.Map;

public final class GXEditorResponseInfo {
    public static final int SUCCESS = 0;
    public static final int MAX_SIZE = 1;
    public static final int PERMISSION_DENIED = 2;
    public static final int FAILED_CREATE_FILE = 3;
    public static final int IO_ERROR = 4;
    public static final int NOT_MULTIPART_CONTENT = 5;
    public static final int PARSE_REQUEST_ERROR = 6;
    public static final int NOTFOUND_UPLOAD_DATA = 7;
    public static final int NOT_ALLOW_FILE_TYPE = 8;

    public static final int INVALID_ACTION = 101;
    public static final int CONFIG_ERROR = 102;

    public static final int PREVENT_HOST = 201;
    public static final int CONNECTION_ERROR = 202;
    public static final int REMOTE_FAIL = 203;

    public static final int NOT_DIRECTORY = 301;
    public static final int NOT_EXIST = 302;

    public static final int ILLEGAL = 401;

    protected static final Map<Integer, String> info = new HashMap<>();

    static {
        info.put(GXEditorResponseInfo.SUCCESS, "SUCCESS");
        // 无效的Action
        info.put(GXEditorResponseInfo.INVALID_ACTION, "无效的Action");
        // 配置文件初始化失败
        info.put(GXEditorResponseInfo.CONFIG_ERROR, "配置文件初始化失败");
        // 抓取远程图片失败
        info.put(GXEditorResponseInfo.REMOTE_FAIL, "抓取远程图片失败");
        // 被阻止的远程主机
        info.put(GXEditorResponseInfo.PREVENT_HOST, "被阻止的远程主机");
        // 远程连接出错
        info.put(GXEditorResponseInfo.CONNECTION_ERROR, "远程连接出错");
        // 文件大小超出限制
        info.put(GXEditorResponseInfo.MAX_SIZE, "文件大小超出限制");
        // 权限不足， 多指写权限
        info.put(GXEditorResponseInfo.PERMISSION_DENIED, "权限不足， 多指写权限");
        // 创建文件失败
        info.put(GXEditorResponseInfo.FAILED_CREATE_FILE, "创建文件失败");
        // IO错误
        info.put(GXEditorResponseInfo.IO_ERROR, "IO错误");
        // 上传表单不是multipart/form-data类型
        info.put(GXEditorResponseInfo.NOT_MULTIPART_CONTENT, "上传表单不是multipart/form-data类型");
        // 解析上传表单错误
        info.put(GXEditorResponseInfo.PARSE_REQUEST_ERROR, "解析上传表单错误");
        // 未找到上传数据
        info.put(GXEditorResponseInfo.NOTFOUND_UPLOAD_DATA, "未找到上传数据");
        // 不允许的文件类型
        info.put(GXEditorResponseInfo.NOT_ALLOW_FILE_TYPE, "不允许的文件类型");
        // 指定路径不是目录
        info.put(GXEditorResponseInfo.NOT_DIRECTORY, "指定路径不是目录");
        // 指定路径并不存在
        info.put(GXEditorResponseInfo.NOT_EXIST, "指定路径并不存在");
        // callback参数名不合法
        info.put(GXEditorResponseInfo.ILLEGAL, "callback参数名不合法");
    }

    private GXEditorResponseInfo() {
    }

    static String getStateInfo(int key) {
        return GXEditorResponseInfo.info.get(key);
    }
}
