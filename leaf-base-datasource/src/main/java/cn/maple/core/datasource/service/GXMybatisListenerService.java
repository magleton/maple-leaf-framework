package cn.maple.core.datasource.service;

import cn.hutool.core.lang.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface GXMybatisListenerService<T> {
    Logger LOG = LoggerFactory.getLogger(GXMybatisListenerService.class);

    /**
     * 保存实体的监听器
     *
     * @param data 实体数据
     */
    default void saveEntityListener(T data) {
        LOG.info("请自定义实现saveEntityListener监听逻辑");
    }

    /**
     * 更新实体的监听器
     *
     * @param data             更新之后的实体数据
     * @param keyValuePairs    本次更新的条件
     * @param keyOperatorPairs 每个字段的操作
     */
    default void updateEntityListener(T data, Dict keyValuePairs, Dict keyOperatorPairs) {
        LOG.info("请自定义实现updateEntityListener监听逻辑");
    }

    /**
     * 更新字段的监听器
     *
     * @param updateFieldData 更新的字段信息
     * @param conditionFieldData  更新条件
     */
    default void updateFieldListener(Dict updateFieldData, Dict conditionFieldData) {
        LOG.info("请自定义实现updateFieldListener监听逻辑");
    }

    /**
     * 软删除监听器
     *
     * @param data 删除的条件
     */
    default void deleteSoftListener(Dict data) {
        LOG.info("请自定义实现deleteSoftListener监听逻辑");
    }
}
