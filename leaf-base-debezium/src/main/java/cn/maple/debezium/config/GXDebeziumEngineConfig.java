package cn.maple.debezium.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.debezium.exception.GXDebeziumInitialException;
import cn.maple.debezium.properties.GXDebeziumProperties;
import cn.maple.debezium.services.GXDebeziumService;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.*;

@Configuration
@Log4j2
public class GXDebeziumEngineConfig implements DisposableBean {
    private final Map<String, DebeziumEngine<ChangeEvent<String, String>>> debeziumEngineMap = new ConcurrentHashMap<>();

    private final Map<String, ExecutorService> debeziumEngineExecutorMap = new ConcurrentHashMap<>();

    @Resource
    private GXDebeziumProperties debeziumProperties;

    @PostConstruct
    public void initDebeziumEngine() {
        GXDebeziumService debeziumService = GXSpringContextUtils.getBean(GXDebeziumService.class);
        if (ObjectUtil.isNull(debeziumService)) {
            log.error("请实现GXDebeziumService接口");
            return;
        }
        debeziumProperties.getConfig().forEach((key, map) -> {
            debeziumService.initialEngineLock(key);
            Properties properties = new Properties();
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }
            try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                    .using(properties)
                    .notifying(record -> {
                        log.debug("监听到数据库数据变化 : {}", record);
                        String value = record.value();
                        Dict dbChangeData = JSONUtil.toBean(value, Dict.class);
                        Dict payload = Convert.convert(Dict.class, dbChangeData.getObj("payload"));
                        debeziumService.processCaptureDataChange(payload);
                    }).build()
            ) {
                Thread.Builder.OfVirtual ofVirtual = Thread.ofVirtual().name("debezium-virtual-thread#", 1);
                ThreadFactory factory = ofVirtual.factory();
                ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory);
                executorService.execute(engine);
                debeziumEngineMap.put(key, engine);
                debeziumEngineExecutorMap.put(key, executorService);
            } catch (IOException e) {
                throw new GXDebeziumInitialException(CharSequenceUtil.format("初始化Debezium内嵌引擎{}失败", key), e);
            } finally {
                debeziumService.initialEngineUnLock(key);
            }
        });
    }

    /**
     * Invoked by the containing {@code BeanFactory} on destruction of a bean.
     *
     * @throws Exception in case of shutdown errors. Exceptions will get logged
     *                   but not rethrown to allow other beans to release their resources as well.
     */
    @Override
    public void destroy() throws Exception {
        String appName = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class);
        for (Map.Entry<String, DebeziumEngine<ChangeEvent<String, String>>> entry : debeziumEngineMap.entrySet()) {
            String key = entry.getKey();
            DebeziumEngine<ChangeEvent<String, String>> engine = entry.getValue();
            engine.close();
            ExecutorService executorService = debeziumEngineExecutorMap.remove(key);
            executorService.shutdown();
            while (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                log.info("Waiting another 60 seconds for the embedded engine to shut down");
            }
        }
        log.info("~~~~ 应用{}的Debezium引擎关闭完成 , 再见 ~~~~~", appName);
    }
}
