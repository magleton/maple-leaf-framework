package cn.maple.core.framework.service;

import cn.maple.core.framework.controller.GXBase64DecodedMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public interface GXFileUploadService {
    /**
     * 上传文件
     *
     * @param file 上传的文件对象
     * @return 文件存储路径
     */
    String upload(MultipartFile file);

    /**
     * 上传Base64文件
     * Controller可以直接使用GXBase64DecodedMultipartFile类来接收前端传递的Base64字符串
     *
     * @param file Base64编码的文件
     * @return 文件名字
     */
    String upload(GXBase64DecodedMultipartFile file);

    /**
     * 获取上传文件存储目录
     *
     * @return 文件上传路径
     */
    String getStoragePath();

    /**
     * 删除指定文件
     *
     * @param filename 待删除的文件名字
     * @return 删除是否成功
     */
    boolean deleteFile(String filename);
}
