package com.geoxus.ueditor;

import com.geoxus.ueditor.define.GXBaseState;
import com.geoxus.ueditor.define.GXEditorResponseInfo;
import com.geoxus.ueditor.define.GXState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;

@Slf4j
public class GXStorageManager {
    private static final int BUFFER_SIZE = 8192;

    private GXStorageManager() {
    }

    public static GXState saveBinaryFile(byte[] data, String path) {
        File file = new File(path);
        GXState state = valid(file);
        if (!state.isSuccess()) {
            return state;
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            bos.write(data);
            bos.flush();
        } catch (IOException ioe) {
            return new GXBaseState(false, GXEditorResponseInfo.IO_ERROR);
        }
        state = new GXBaseState(true, file.getAbsolutePath());
        state.putInfo("size", data.length);
        state.putInfo("title", file.getName());
        return state;
    }

    public static GXState saveFileByInputStream(InputStream is, String path, long maxSize) {
        GXState state = null;
        File tmpFile = getTmpFile();
        byte[] dataBuf = new byte[2048];
        BufferedInputStream bis = new BufferedInputStream(is, GXStorageManager.BUFFER_SIZE);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile), GXStorageManager.BUFFER_SIZE)) {
            int count = 0;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }
            bos.flush();
            if (tmpFile.length() > maxSize) {
                Files.delete(tmpFile.toPath());
                return new GXBaseState(false, GXEditorResponseInfo.MAX_SIZE);
            }
            state = saveTmpFile(tmpFile, path);
            if (!state.isSuccess()) {
                Files.delete(tmpFile.toPath());
            }
            return state;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new GXBaseState(false, GXEditorResponseInfo.IO_ERROR);
    }

    public static GXState saveFileByInputStream(InputStream is, String path) {
        GXState state = null;
        File tmpFile = getTmpFile();
        byte[] dataBuf = new byte[2048];
        BufferedInputStream bis = new BufferedInputStream(is, GXStorageManager.BUFFER_SIZE);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile), GXStorageManager.BUFFER_SIZE)) {
            int count = 0;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }
            bos.flush();
            state = saveTmpFile(tmpFile, path);
            if (!state.isSuccess()) {
                Files.delete(tmpFile.toPath());
            }
            return state;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new GXBaseState(false, GXEditorResponseInfo.IO_ERROR);
    }

    private static File getTmpFile() {
        File tmpDir = FileUtils.getTempDirectory();
        String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
        return new File(tmpDir, tmpFileName);
    }

    private static GXState saveTmpFile(File tmpFile, String path) {
        GXState state = null;
        File targetFile = new File(path);
        if (targetFile.canWrite()) {
            return new GXBaseState(false, GXEditorResponseInfo.PERMISSION_DENIED);
        }
        try {
            FileUtils.moveFile(tmpFile, targetFile);
        } catch (IOException e) {
            return new GXBaseState(false, GXEditorResponseInfo.IO_ERROR);
        }

        state = new GXBaseState(true);
        state.putInfo("size", targetFile.length());
        state.putInfo("title", targetFile.getName());
        return state;
    }

    private static GXState valid(File file) {
        File parentPath = file.getParentFile();
        if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
            return new GXBaseState(false, GXEditorResponseInfo.FAILED_CREATE_FILE);
        }
        if (!parentPath.canWrite()) {
            return new GXBaseState(false, GXEditorResponseInfo.PERMISSION_DENIED);
        }
        return new GXBaseState(true);
    }
}
