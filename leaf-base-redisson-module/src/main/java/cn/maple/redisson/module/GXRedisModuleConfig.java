package cn.maple.redisson.module;

import io.github.dengliming.redismodule.redisearch.client.RediSearchClient;
import io.github.dengliming.redismodule.redisjson.client.RedisJSONClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class GXRedisModuleConfig {
    @Resource
    private Config config;

    @Bean
    public RedisJSONClient redisJSONClient() {
        return new RedisJSONClient(config);
    }

    @Bean
    public RediSearchClient rediSearchClient() {
        return new RediSearchClient(config);
    }
}
