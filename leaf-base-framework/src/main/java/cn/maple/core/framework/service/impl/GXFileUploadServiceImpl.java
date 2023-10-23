package cn.maple.core.framework.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.maple.core.framework.controller.GXBase64DecodedMultipartFile;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXFileUploadService;
import cn.maple.core.framework.util.GXCommonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Log4j2
@Service
public class GXFileUploadServiceImpl implements GXFileUploadService {
    /**
     * 获取文件扩展名字
     *
     * @param fileName 文件名字
     * @return 文件扩展名 .jpg .word
     */
    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        return com.google.common.io.Files.getFileExtension(fileName);
    }

    /**
     * 上传文件
     *
     * @param file 上传的文件对象
     * @return 文件名字
     */
    @Override
    public String upload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new GXBusinessException("文件不能为空");
            }
            Path targetLocation = getFileStoragePath(getFileExtension(file.getName()));
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.getFileName().toString();
        } catch (IOException ex) {
            throw new GXBusinessException("不能保存文件.请重试!", ex);
        }
    }

    /**
     * 上传Base64文件
     * Controller可以直接使用GXBase64DecodedMultipartFile类来接收前端传递的Base64字符串
     *
     * @param file Base64编码的文件
     * @return 文件名字
     */
    @Override
    public String upload(GXBase64DecodedMultipartFile file) {
        try {
            String type = getFileExtension(file.getOriginalFilename());
            Path fileStoragePath = getFileStoragePath(type);
            file.transferTo(fileStoragePath);
            return fileStoragePath.getFileName().toString();
        } catch (IOException ex) {
            throw new GXBusinessException("不能保存文件.请重试!", ex);
        }
    }

    private Path getFileStoragePath(String mediaType) throws IOException {
        String envStoragePath = GXCommonUtils.getEnvironmentValue("upload.depositPath", String.class, "./uploads/files");
        Path rootLocation = Paths.get(envStoragePath).normalize();
        if (Files.notExists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }
        String newFileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + RandomUtil.getRandom().nextInt(0, 999) + "." + mediaType;
        if (newFileName.contains("..")) {
            throw new GXBusinessException("对不起!文件包含无效的路径符号" + newFileName);
        }
        Path destinationFile = rootLocation.resolve(Paths.get(newFileName)).toAbsolutePath();
        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new GXBusinessException("不存存储文件到当前目录之外.");
        }
        return destinationFile;
    }
}