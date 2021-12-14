package cn.gaple.extension.register;

import cn.gaple.extension.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXLoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

/**
 * ExtensionRegister 扩展注册器
 *
 * @author britton
 */
@Component
@Slf4j
public class GXExtensionRegister {
    /**
     * 扩展点的名字
     */
    public static final String EXTENSION_EXT_PT_NAMING = "ExtPoint";

    @Resource
    private GXExtensionRepository extensionRepository;

    public void doRegistration(GXExtensionPoint extensionObject) {
        Class<?> extensionClz = extensionObject.getClass();
        if (AopUtils.isAopProxy(extensionObject)) {
            extensionClz = ClassUtils.getUserClass(extensionObject);
        }
        GXExtension extensionAnn = AnnotationUtils.findAnnotation(extensionClz, GXExtension.class);
        if (Objects.nonNull(extensionAnn)) {
            GXBizScenario bizScenario = GXBizScenario.valueOf(extensionAnn.bizId(), extensionAnn.useCase(), extensionAnn.scenario());
            GXExtensionCoordinate extensionCoordinate = new GXExtensionCoordinate(calculateExtensionPoint(extensionClz), bizScenario.getUniqueIdentity());
            GXExtensionPoint preVal = extensionRepository.getExtensionRepo().put(extensionCoordinate, extensionObject);
            if (preVal != null) {
                // 如果已经注册 , 则发出警告信息 , 不影响后面使用
                GXLoggerUtils.logWarn(log, CharSequenceUtil.format("Duplicate registration is not allowed for : {}", extensionCoordinate));
            }
        }
    }

    /**
     * @param targetClz 目标类型
     * @return 扩展点名字
     */
    private String calculateExtensionPoint(Class<?> targetClz) {
        Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(targetClz);
        if (CollUtil.isEmpty(Arrays.asList(interfaces))) {
            throw new GXBusinessException("Please assign a extension point interface for " + targetClz);
        }
        for (Class<?> clazz : interfaces) {
            String extensionPoint = clazz.getSimpleName();
            if (extensionPoint.contains(EXTENSION_EXT_PT_NAMING)) {
                return clazz.getName();
            }
        }
        throw new GXBusinessException("Your name of ExtensionPoint for " + targetClz + " is not valid, must be end of " + EXTENSION_EXT_PT_NAMING);
    }
}
