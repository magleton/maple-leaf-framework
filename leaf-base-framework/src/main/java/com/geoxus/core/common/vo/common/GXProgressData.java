package com.geoxus.core.common.vo.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class GXProgressData implements Serializable {
    //到目前为止读取文件的比特数
    private long pBytesRead = 0L;

    //文件总大小 
    private long pContentLength = 0L;

    //目前正在读取第几个文件
    private int pItems;
}

