package com.geoxus.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class GXBaseData implements Serializable {
    private static final long serialVersionUID = -8579692140225188157L;

    /**
     * @author britton
     * 对请求参数进行补充校验
     */
    protected void verify() {
    }

    /**
     * @author britton
     * 对参数进行补充修复(自动填充一些信息)
     */
    protected void repair() {
    }

    /**
     * @author britton
     * 调用自定义的方法进行参数的处理
     */
    public void customizeProcess() {
        this.repair();
        this.verify();
    }
}
