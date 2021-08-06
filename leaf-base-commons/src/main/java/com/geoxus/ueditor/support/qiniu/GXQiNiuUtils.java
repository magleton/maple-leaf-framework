package com.geoxus.ueditor.support.qiniu;

import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.ueditor.config.GXEditorProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.common.ZoneReqInfo;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 七牛云工具类
 */
@Slf4j
public class GXQiNiuUtils {
    private static final GXEditorProperties properties = GXSpringContextUtils.getBean(GXEditorProperties.class);

    private GXQiNiuUtils() {
    }

    /**
     * 文件上传
     *
     * @param file     上传的文件
     * @param fileName 自定义文件名
     * @return java.lang.String
     */
    public static String upload(MultipartFile file, String fileName) {
        String zoneStr = properties.getQiniu().getZone();
        Zone zone = getByName(zoneStr);
        Configuration cfg = new Configuration(zone);
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = properties.getQiniu().getAccessKey();
        String secretKey = properties.getQiniu().getSecretKey();
        String bucket = properties.getQiniu().getBucket();
        try {
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(file.getBytes());
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(byteInputStream, fileName, upToken, null, null);
            //解析上传成功的结果
            //DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
            return properties.getQiniu().getCdn() + putRet.key;
        } catch (QiniuException ex) {
            log.error(ex.getMessage(), ex);
            return null;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 文件上传
     *
     * @param inputStream 上传的文件流
     * @param fileName    自定义文件名
     * @return java.lang.String
     */
    public static String upload(InputStream inputStream, String fileName) {
        Configuration cfg = new Configuration(getByName(properties.getQiniu().getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Auth auth = Auth.create(properties.getQiniu().getAccessKey(), properties.getQiniu().getSecretKey());
            try {
                Response response = uploadManager.put(inputStream, fileName, auth.uploadToken(properties.getQiniu().getBucket()), null, null);
                //解析上传成功的结果
                //DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
                return properties.getQiniu().getCdn() + putRet.key;
            } catch (QiniuException ex) {
                log.error(ex.getMessage(), ex);
                return null;
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * base64图片上传
     *
     * @param content  图片内筒
     * @param fileName 自定义文件名
     * @return java.lang.String
     */
    public static String upload(String content, String fileName) {
        Zone zone = getByName(properties.getQiniu().getZone());
        String accessKey = properties.getQiniu().getAccessKey();
        String secretKey = properties.getQiniu().getSecretKey();
        String bucket = properties.getQiniu().getBucket();
        String url = zone.getUpBackupHttp(new ZoneReqInfo(accessKey, bucket)) + "/putb64/" + "-1" + "/key/" + UrlSafeBase64.encodeToString(fileName);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        RequestBody rb = RequestBody.create(null, content);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + upToken)
                .post(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        if (null != response) {
            //DefaultPutRet putRet = new Gson().fromJson(response.body().charStream(), DefaultPutRet.class);
            DefaultPutRet putRet = null;
            try {
                assert response.body() != null;
                putRet = JSONUtil.toBean(response.body().string(), DefaultPutRet.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert putRet != null;
            if (putRet.key == null) {
                return null;
            }
            return properties.getQiniu().getCdn() + putRet.key;
        }
        return "";
    }

    /**
     * @param prefix 文件前缀
     * @param index  从第几个开始
     * @param size   返回条数
     * @param total  总条数
     * @return 文件列表
     */
    public static List<String> listFile(String prefix, int index, int size, GXTotal total) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        String accessKey = properties.getQiniu().getAccessKey();
        String secretKey = properties.getQiniu().getSecretKey();
        String bucket = properties.getQiniu().getBucket();
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        // 每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        // 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        // 列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
        FileInfo[] items = null;
        // 只列举最近的1000个文件多了也不利于展示
        if (fileListIterator.hasNext()) {
            //处理获取的file list结果
            items = fileListIterator.next();
        }
        if (items == null) {
            return Collections.emptyList();
        }
        total.setTotal(items.length);
        if (index < 0 || index > items.length) {
            return Collections.emptyList();
        }
        return Arrays.stream(items).skip(index).limit(size).map(fileInfo -> properties.getQiniu().getCdn() + fileInfo.key).collect(Collectors.toList());
    }

    /**
     * 根据名称获取对应的上传区域
     *
     * @param name zone name
     * @return com.qiniu.common.Zone
     * @author lihy
     */
    static Zone getByName(String name) {
        Zone zone = null;
        switch (name) {
            case "zone0":
                zone = Zone.zone0();
                break;
            case "zone1":
                zone = Zone.zone1();
                break;
            case "zone2":
                zone = Zone.zone2();
                break;
            case "zoneNa0":
                zone = Zone.zoneNa0();
                break;
            case "zoneAs0":
                zone = Zone.zoneAs0();
                break;
            default:
                throw new GXException("百度编辑七牛云zone配置错误，https://developer.qiniu.com/kodo/sdk/1239/java#server-upload");
        }
        return zone;
    }
}
