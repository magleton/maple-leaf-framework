package cn.maple.extension.register;

import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.extension.GXExtension;
import cn.maple.extension.GXExtensionPoint;
import cn.maple.extension.GXExtensions;
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

    /**
     * 应用启动时自动注册所有扩展点对象
     *
     * @author britton
     */
    @PostConstruct
    public void init() {
        Map<String, Object> extensionBeans = GXSpringContextUtils.getApplicationContext().getBeansWithAnnotation(GXExtension.class);
        extensionBeans.values().forEach(extension -> extensionRegister.doRegistration((GXExtensionPoint) extension));

        Map<String, Object> extensionsBeans = GXSpringContextUtils.getApplicationContext().getBeansWithAnnotation(GXExtensions.class);
        extensionsBeans.values().forEach(extension -> extensionRegister.doRegistrationExtensions((GXExtensionPoint) extension));
    }
}
