package cn.maple.debezium.framework;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.service.GXCommandLineRunnerService;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.debezium.exception.GXDebeziumInitialException;
import cn.maple.debezium.properties.GXDebeziumProperties;
import cn.maple.debezium.services.GXDebeziumService;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Order
@Slf4j
public class GXDebeziumCommandLineRunnerServiceImpl implements GXCommandLineRunnerService, DisposableBean {
    private final Map<String, DebeziumEngine<ChangeEvent<String, String>>> debeziumEngineMap = new ConcurrentHashMap<>();

    @Resource
    private GXDebeziumProperties debeziumProperties;

    @Resource
    private TaskExecutor debeziumExecutor;

    @Override
    public void run() {
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
                        Dict payload = (Dict) dbChangeData.getObj("payload");
                        debeziumService.processCaptureDataChange(payload);
                    }).build()
            ) {
                debeziumEngineMap.put(key, engine);
                debeziumExecutor.execute(engine);
            } catch (IOException e) {
                throw new GXDebeziumInitialException(CharSequenceUtil.format("初始化Debezium内嵌引擎{}失败", key), e);
            } finally {
                debeziumService.initialEngineUnLock(key);
            }
        });
    }

    @Override
    public void destroy() {
        for (Map.Entry<String, DebeziumEngine<ChangeEvent<String, String>>> entry : debeziumEngineMap.entrySet()) {
            try {
                DebeziumEngine<ChangeEvent<String, String>> engine = entry.getValue();
                engine.close();
            } catch (IOException e) {
                log.error("关闭{}>>>Debezium引擎失败", entry.getKey(), e);
            }
        }
        String appName = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class);
        log.info("~~~~ 应用{}退出了 , 再见 ~~~~~", appName);
    }
}
