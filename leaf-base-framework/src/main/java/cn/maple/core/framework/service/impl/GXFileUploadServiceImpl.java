package cn.maple.core.framework.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.maple.core.framework.controller.GXBase64DecodedMultipartFile;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXFileUploadService;
import cn.maple.core.framework.util.GXCommonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;

@Log4j2
@Service
@ConditionalOnExpression("'${maple.framework.enable.file-upload}'.equals('true')")
public class GXFileUploadServiceImpl implements GXFileUploadService {
    /**
     * 上传文件的存储路径
     */
    private Path fileStoragePath;

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
    public String upload(String relativePath, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new GXBusinessException("文件不能为空");
            }
            Path fileStorageFileName = getFileStoragePath(relativePath, getFileExtension(file.getOriginalFilename()));
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, fileStorageFileName, StandardCopyOption.REPLACE_EXISTING);
            }
            return fileStorageFileName.getFileName().toString();
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
    public String upload(String relativePath, GXBase64DecodedMultipartFile file) {
        try {
            String type = getFileExtension(file.getOriginalFilename());
            Path fileStorageFilename = getFileStoragePath(relativePath, type);
            file.transferTo(fileStorageFilename);
            return fileStorageFilename.getFileName().toString();
        } catch (IOException ex) {
            throw new GXBusinessException("不能保存文件.请重试!", ex);
        }
    }

    /**
     * 获取上传文件存储目录
     *
     * @return 文件上传路径
     */
    @Override
    public String getStoragePath() {
        return fileStoragePath.toAbsolutePath().toString();
    }

    /**
     * 删除指定文件
     *
     * @param filename 待删除的文件名字
     * @return 删除是否成功
     */
    @Override
    public boolean deleteFile(String filename) {
        Path destinationFile = fileStoragePath.resolve(Paths.get(filename)).toAbsolutePath();
        try {
            if (Files.notExists(destinationFile)) {
                log.info("待删除文件{}不存在", destinationFile);
                return true;
            }
            log.info("正在删除文件{}", destinationFile);
            Files.delete(destinationFile);
            log.info("删除文件{}成功", destinationFile);
            return true;
        } catch (Exception ex) {
            throw new GXBusinessException(CharSequenceUtil.format("删除文件{}失败", destinationFile), ex);
        }
    }

    /**
     * 跟传入的文件名字后缀生成文件存储的完成路径
     *
     * @param mediaType 文件类型
     * @return 完成的文件路径
     */
    private Path getFileStoragePath(String relativePath, String mediaType) {
        String newFileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + RandomUtil.getRandom().nextInt(0, 999) + "." + mediaType;
        if (newFileName.contains("..")) {
            throw new GXBusinessException("对不起!文件包含无效的路径符号" + newFileName);
        }
        String envStoragePath = GXCommonUtils.getEnvironmentValue("upload.depositPath", String.class, "./uploads/files");
        String storagePath = CharSequenceUtil.format("{}{}{}", envStoragePath, File.separator, relativePath);
        fileStoragePath = Paths.get(storagePath).normalize();
        if (Files.notExists(fileStoragePath)) {
            synchronized (GXFileUploadServiceImpl.class) {
                try {
                    Files.createDirectories(fileStoragePath);
                } catch (FileAlreadyExistsException e) {
                    log.info("文件夹{}已经存在", fileStoragePath);
                } catch (IOException e) {
                    throw new GXBusinessException(CharSequenceUtil.format("上传文件,创建目录{}失败", fileStoragePath), e);
                }
            }
        }
        Path destinationFile = fileStoragePath.resolve(Paths.get(newFileName)).toAbsolutePath();
        if (!destinationFile.getParent().equals(fileStoragePath.toAbsolutePath())) {
            throw new GXBusinessException("不存存储文件到当前目录之外.");
        }
        return destinationFile;
    }
}