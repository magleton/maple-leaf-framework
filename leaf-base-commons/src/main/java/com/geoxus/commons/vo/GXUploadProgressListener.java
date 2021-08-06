package com.geoxus.commons.vo;

import com.geoxus.core.common.vo.common.GXProgressData;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class GXUploadProgressListener implements ProgressListener {

    private HttpSession session;

    public void setSession(HttpSession session) {
        this.session = session;
        GXProgressData progress = new GXProgressData();
        session.setAttribute("progress", progress);
    }

    /**
     * @param pBytesRead     到目前为止读取文件的比特数
     * @param pContentLength 文件总大小
     * @param pItems         目前正在读取第几个文件
     */
    @Override
    public void update(long pBytesRead, long pContentLength, int pItems) {
        GXProgressData progress = (GXProgressData) session.getAttribute("progress");
        progress.setPBytesRead(pBytesRead);
        progress.setPContentLength(pContentLength);
        progress.setPItems(pItems);
    }
}

