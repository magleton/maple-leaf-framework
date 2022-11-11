package cn.maple.core.framework.service;

import java.util.concurrent.TimeUnit;

public interface GXBaseCacheLock {
    /**
     * 加锁
     *
     * @param lockName 锁的名字
     */
    default void lock(String lockName) {

    }

    /**
     * 尝试加锁
     *
     * @param lockName 锁的名字
     * @param timeOut  等待时间
     */
    default void tryLock(String lockName, TimeUnit timeOut) {

    }

    /**
     * 解锁
     *
     * @param lockName 锁的名字
     */
    default void unlock(String lockName) {

    }
}
