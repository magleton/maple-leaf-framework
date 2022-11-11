package cn.maple.core.framework.service;

import java.util.concurrent.locks.Lock;

public interface GXBaseCacheLockService {
    /**
     * 获取一个锁对象
     *
     * @param lockName 锁的名字
     * @return Lock对象
     */
    default Lock getLock(String lockName) {
        return null;
    }
}
