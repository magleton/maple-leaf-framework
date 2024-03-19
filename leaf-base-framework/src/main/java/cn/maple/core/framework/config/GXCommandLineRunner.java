package cn.maple.core.framework.config;

import cn.maple.core.framework.service.GXCommandLineRunnerService;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
public class GXCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Map<String, GXCommandLineRunnerService> commandLineRunnerServiceBeans = GXSpringContextUtils.getApplicationContext().getBeansOfType(GXCommandLineRunnerService.class);
        if (!commandLineRunnerServiceBeans.isEmpty()) {
            commandLineRunnerServiceBeans.values().forEach(GXCommandLineRunnerService::run);
        }
    }
}
