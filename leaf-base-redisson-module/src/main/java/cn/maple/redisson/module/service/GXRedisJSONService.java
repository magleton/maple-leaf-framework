package cn.maple.redisson.module.service;

import cn.hutool.core.lang.Dict;
import io.github.dengliming.redismodule.redisjson.args.GetArgs;

public interface GXRedisJSONService {
    /**
     * 新增JSON数据
     *
     * @param key  key的名字
     * @param path JSON的路径
     * @param data JSON数据
     * @return 是否添加成功
     */
    boolean set(String key, String path, Dict data);

    /**
     * 获取指定key的数据
     *
     * @param key   key的名字
     * @param clazz 返回数据的类型
     * @param args  获取的参数
     * @return 指定的数据类型
     */
    <T> T get(String key, Class<T> clazz, GetArgs args);

    /**
     * 删除指定的key
     *
     * @param key  key的名字
     * @param path 需要删除的JSON对象的路径
     * @return 删除的条数
     */
    long del(String key, String path);
}
