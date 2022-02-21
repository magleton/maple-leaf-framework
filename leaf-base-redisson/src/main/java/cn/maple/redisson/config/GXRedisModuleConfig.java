package cn.maple.redisson.config;

import io.github.dengliming.redismodule.redisearch.client.RediSearchClient;
import io.github.dengliming.redismodule.redisjson.client.RedisJSONClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@Slf4j
@ConditionalOnClass(name = "io.github.dengliming.redismodule.common.util.ArgsUtil")
public class GXRedisModuleConfig {
    @Resource
    private Config config;

    @Bean
    @ConditionalOnClass(name = "io.github.dengliming.redismodule.redisjson.client.RedisJSONClient")
    public RedisJSONClient redisJSONClient() {
        return new RedisJSONClient(config);
    }

    @Bean
    @ConditionalOnClass(name = "io.github.dengliming.redismodule.redisearch.client.RediSearchClient")
    public RediSearchClient rediSearchClient() {
        return new RediSearchClient(config);
    }
}
