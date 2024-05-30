package cn.maple.core.framework.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Tomcat 配置
 */
@Configuration
@SuppressWarnings("all")
public class GXTomcatConfig {
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            Thread.Builder.OfVirtual ofVirtual = Thread.ofVirtual().name("virtual-tomcat#", 1);
            ThreadFactory factory = ofVirtual.factory();
            protocolHandler.setExecutor(Executors.newThreadPerTaskExecutor(factory));
        };
    }
}