package cn.maple.core.framework.dto;

import java.io.Serializable;

public interface GXBaseData extends Serializable {
    /**
     * @author britton
     * 在验证请求参数之前进行数据修复(自动填充一些信息)
     */
    default void beforeRepair() {
    }

    /**
     * @author britton
     * 对请求参数进行补充校验
     */
    default void verify() {
    }

    /**
     * @author britton
     * 参数
     * 验证请求参数之后进行数据修复(自动填充一些信息)
     */
    default void afterRepair() {
    }

    /**
     * @author britton
     * 调用自定义的方法进行参数的处理
     */
    default void customizeProcess() {
        this.beforeRepair();
        this.verify();
        this.afterRepair();
    }
}
