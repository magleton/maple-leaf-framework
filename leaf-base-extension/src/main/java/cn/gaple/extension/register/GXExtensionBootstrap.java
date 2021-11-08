package cn.gaple.extension.register;

import cn.gaple.extension.GXExtension;
import cn.gaple.extension.GXExtensionPoint;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * GXExtensionBootstrap  扩展启动类
 *
 * @author britton
 */
@Component
public class GXExtensionBootstrap {
    @Resource
    private GXExtensionRegister extensionRegister;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 应用启动时自动注册所有扩展点对象
     *
     * @author britton
     */
    @PostConstruct
    public void init() {
        Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(GXExtension.class);
        extensionBeans.values().forEach(
                extension -> extensionRegister.doRegistration((GXExtensionPoint) extension)
        );
    }
}
