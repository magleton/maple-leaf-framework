package cn.maple.extension.autoconfigure;

import cn.maple.extension.GXExtensionExecutor;
import cn.maple.extension.GXExtensionRepository;
import cn.maple.extension.register.GXExtensionBootstrap;
import cn.maple.extension.register.GXExtensionRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtensionAutoConfiguration {
    @Bean(value = "extensionBootstrap", initMethod = "init")
    @ConditionalOnMissingBean(GXExtensionBootstrap.class)
    public GXExtensionBootstrap extPointBootstrap() {
        return new GXExtensionBootstrap();
    }

    @Bean("extensionRepository")
    @ConditionalOnMissingBean(GXExtensionRepository.class)
    public GXExtensionRepository extPointRepository() {
        return new GXExtensionRepository();
    }

    @Bean(value = "extensionExecutor")
    @ConditionalOnMissingBean(GXExtensionExecutor.class)
    public GXExtensionExecutor extensionExecutor() {
        return new GXExtensionExecutor();
    }

    @Bean(value = "extensionRegister")
    @ConditionalOnMissingBean(GXExtensionRegister.class)
    public GXExtensionRegister extensionRegister() {
        return new GXExtensionRegister();
    }
}
