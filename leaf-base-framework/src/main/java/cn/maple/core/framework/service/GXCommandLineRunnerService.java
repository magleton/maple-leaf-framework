package cn.maple.core.framework.service;

/**
 * 应用启动完成之后需要执行的逻辑
 * 比如 预热缓存
 */
public interface GXCommandLineRunnerService {
    void run();
}
