package cn.maple.core.framework.dto;

import cn.hutool.core.bean.copier.CopyOptions;

import java.io.Serializable;

public abstract class GXBaseData implements Serializable {
    /**
     * @author britton
     * 在验证请求参数之前进行数据修复(自动填充一些信息)
     */
    protected void beforeRepair() {
    }

    /**
     * @author britton
     * 对请求参数进行补充校验
     */
    protected void verify() {
    }

    /**
     * @author britton
     * 参数
     * 验证请求参数之后进行数据修复(自动填充一些信息)
     */
    protected void afterRepair() {
    }

    /**
     * 转换之前的回调
     */
    protected void beforeMapping(CopyOptions copyOptions) {
        customizeProcess();
    }

    /**
     * 转换完成之后的回调
     */
    protected void afterMapping(Object source) {
        customizeProcess();
    }

    /**
     * @author britton
     * 调用自定义的方法进行参数的处理
     */
    public void customizeProcess() {
        this.beforeRepair();
        this.verify();
        this.afterRepair();
    }
}
