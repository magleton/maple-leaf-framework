package cn.maple.debezium.services;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.redisson.util.GXRedissonUtils;

import java.util.concurrent.TimeUnit;

public interface GXDebeziumService {
    /**
     * 自定义业务处理
     *
     * @param data 数据库变化的数据
     */
    void processCaptureDataChange(Dict data);

    /**
     * 在同一个服务部署了多个实例的情况下
     * 只需要有一个服务是正常处理CDC的即可
     * 所以需要在服务启动时 判断当前服务监听的数据库是否已经有相应的
     * Debezium实例在服务
     * 初始化Debezium引擎上锁
     *
     * @param key 实例的key
     */
    default void initialEngineLock(String key) {
        String appName = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class);
        String lockName = CharSequenceUtil.format("debezium-initial-engine:{}:{}", appName, key);
        GXRedissonUtils.getLock(lockName).lock(10, TimeUnit.SECONDS);
    }

    /**
     * 在同一个服务部署了多个实例的情况下
     * 只需要有一个服务是正常处理CDC的即可
     * 所以需要在服务启动时 判断当前服务监听的数据库是否已经有相应的
     * Debezium实例在服务
     * 初始化Debezium引擎解锁
     *
     * @param key 实例的key
     */
    default void initialEngineUnLock(String key) {
        String appName = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class);
        String lockName = CharSequenceUtil.format("debezium-initial-engine:{}:{}", appName, key);
        GXRedissonUtils.getLock(lockName).unlock();
    }
}
