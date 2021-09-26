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
    public void verify() {
    }

    /**
     * @author britton
     * 对参数进行补充修复(自动填充一些信息)
     */
    public void repair() {
    }
}
