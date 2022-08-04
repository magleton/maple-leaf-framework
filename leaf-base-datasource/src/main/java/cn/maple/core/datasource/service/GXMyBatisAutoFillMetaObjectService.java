package cn.maple.core.datasource.service;

/**
 * MyBatis Plus的自动填充数据获取
 *
 * @author 塵渊  britton@126.com
 */
public interface GXMyBatisAutoFillMetaObjectService {
    /**
     * 获取创建者
     *
     * @return 创建者
     */
    default String getCreatedBy() {
        return "unknown";
    }

    /**
     * 获取更新者
     *
     * @return 更新者
     */
    default String getUpdatedBy() {
        return "unknown";
    }
}
