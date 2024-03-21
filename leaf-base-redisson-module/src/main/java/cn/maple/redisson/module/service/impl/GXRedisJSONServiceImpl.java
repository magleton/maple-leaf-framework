package cn.maple.redisson.module.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.redisson.module.service.GXRedisJSONService;
import io.github.dengliming.redismodule.redisjson.RedisJSON;
import io.github.dengliming.redismodule.redisjson.args.GetArgs;
import io.github.dengliming.redismodule.redisjson.args.SetArgs;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class GXRedisJSONServiceImpl implements GXRedisJSONService {
    @Resource
    private RedisJSON redisJSON;

    /**
     * 新增JSON数据
     *
     * @param key  key的名字
     * @param path JSON的路径
     * @param data JSON数据
     * @return 是否添加成功
     */
    @Override
    public boolean set(String key, String path, Dict data) {
        SetArgs args = SetArgs.Builder.create(path, JSONUtil.toJsonStr(data));
        String setResult = redisJSON.set(key, args);
        return CharSequenceUtil.equalsIgnoreCase(setResult, "OK");
    }

    /**
     * 获取指定key的数据
     *
     * @param key   key的名字
     * @param clazz 返回数据的类型
     * @param args  获取的参数
     * @return 指定的数据类型
     */
    @Override
    public <T> T get(String key, Class<T> clazz, GetArgs args) {
        return redisJSON.get(key, clazz, args);
    }

    /**
     * 删除指定的key
     *
     * @param key  key的名字
     * @param path 需要删除的JSON对象的路径
     * @return 删除路径的个数
     */
    @Override
    public long del(String key, String path) {
        return redisJSON.del(key, path);
    }

    /**
     * 获取多个key的值
     *
     * @param path  路径
     * @param clazz 目标类型
     * @param keys  需要获取的key列表
     * @return 列表
     */
    @Override
    public <T> List<T> mget(String path, Class<T> clazz, String... keys) {
        return redisJSON.mget(path, clazz, keys);
    }

    /**
     * 获取指定path的JSON类型
     *
     * @param key  key
     * @param path 路径
     * @return Class
     */
    @Override
    public Class<?> getType(String key, String path) {
        return redisJSON.getType(key, path);
    }

    /**
     * 获取RedisJSON对象
     *
     * @return RedisJSON对象
     */
    public RedisJSON getRedisJSON() {
        return redisJSON;
    }
}
