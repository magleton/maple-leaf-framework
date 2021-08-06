package com.geoxus.core.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GXUploadUtils {
    private GXUploadUtils() {
    }

    /**
     * 单文件上传
     *
     * @param file 文件
     * @param path 路径
     * @return String
     * @throws IOException
     */
    public static String singleUpload(MultipartFile file, String path) throws IOException {
        if (mkdirs(path)) {
            return "";
        }
        String fileName;
        fileName = file.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf(('.')) + 1);
        String newFileName = IdUtil.randomUUID() + "." + suffix;
        byte[] bytes = file.getBytes();
        try (BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(path, newFileName)))) {
            buffStream.write(bytes);
            buffStream.flush();
        }
        return newFileName;
    }

    /**
     * 多文件上传
     *
     * @param files 文件
     * @param path  路径
     * @return List
     * @throws IOException
     */
    public static List<Dict> multiUpload(MultipartFile[] files, String path) throws IOException {
        if (mkdirs(path)) {
            return Collections.emptyList();
        }
        String fileName;
        List<Dict> retList = new ArrayList<>();
        for (MultipartFile file : files) {
            fileName = singleUpload(file, path);
            final Dict dict = Dict.create()
                    .set("size", file.getSize())
                    .set("file_name", fileName)
                    .set("disk", path)
                    .set("mime_type", file.getContentType())
                    .set("name", file.getOriginalFilename())
                    .set("file_path", fileName)
                    .set("collection_name", "default");
            retList.add(dict);
        }
        return retList;
    }

    /**
     * 创建上传目录
     *
     * @param path 路径
     * @return boolean
     */
    private static boolean mkdirs(String path) {
        File dirFile = new File(path);
        if (!dirFile.exists() && !dirFile.isDirectory()) {
            return !dirFile.mkdirs();
        }
        return false;
    }

    /**
     * Base64文件上传
     *
     * @param base64 文件base64编码信息
     * @param path   文件存放路径
     * @return String
     * @throws IOException
     */
    public static String base64Upload(String base64, String path) throws IOException {
        if (mkdirs(path)) {
            return "";
        }
        String base64Prefix = ";base64,";
        int endIndex = base64.indexOf(base64Prefix) + base64Prefix.length();
        String imgData = base64.substring(endIndex);
        byte[] bytes = Base64.decode(imgData);
        String suffix = base64.substring(0, endIndex)
                .replace("data:image/", "").replace(base64Prefix, "");
        String fileName = IdUtil.randomUUID() + "." + suffix;
        File file = new File(path, fileName);
        FileUtils.writeByteArrayToFile(file, bytes);
        return fileName;
    }
}
