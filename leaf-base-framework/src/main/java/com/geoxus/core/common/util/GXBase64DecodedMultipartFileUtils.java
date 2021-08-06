package com.geoxus.core.common.util;

import cn.hutool.core.codec.Base64;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;

@SuppressWarnings("unused")
public class GXBase64DecodedMultipartFileUtils implements MultipartFile {

    private byte[] imageBytes;

    private String base64;

    private String contentType;

    public GXBase64DecodedMultipartFileUtils(String file) {
        this.base64 = file;
        this.imageBytes = Base64.decode(base64.substring(base64.indexOf(',') + 1));
        this.contentType = base64.substring(base64.indexOf(':') + 1, base64.indexOf(';'));
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.imageBytes = Base64.decode(base64.substring(base64.indexOf(',') + 1));
        this.contentType = base64.substring(base64.indexOf(':') + 1, base64.indexOf(';'));
        this.base64 = base64;
    }

    @Override
    public String getName() {
        return "base64." + contentType.substring(contentType.indexOf('/') + 1);
    }

    @Override
    public String getOriginalFilename() {
        return getName();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return imageBytes == null || imageBytes.length == 0;
    }

    @Override
    public long getSize() {
        return imageBytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imageBytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imageBytes);
    }

    @SuppressWarnings("resource")
    @Override
    public void transferTo(@NotNull File dest) throws IOException {
        try (final FileOutputStream outputStream = new FileOutputStream(dest)) {
            outputStream.write(imageBytes);
        }
    }
}
