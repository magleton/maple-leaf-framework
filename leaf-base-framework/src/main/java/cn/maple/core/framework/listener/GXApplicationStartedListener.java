package cn.maple.core.framework.listener;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.annotation.GXPermission;
import cn.maple.core.framework.annotation.GXPermissionCtl;
import cn.maple.core.framework.dto.inner.permission.GXBasePermissionInnerDto;
import cn.maple.core.framework.event.GXPermissionEvent;
import cn.maple.core.framework.util.GXEventPublisherUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile({"dev", "local", "test"})
public class GXApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        ConcurrentHashMap<String, List<GXBasePermissionInnerDto>> concurrentHashMap = new ConcurrentHashMap<>();
        Map<String, Object> beansWithAnnotation = applicationStartedEvent.getApplicationContext().getBeanFactory().getBeansWithAnnotation(GXPermissionCtl.class);
        beansWithAnnotation.forEach((k, v) -> {
            Method[] declaredMethods = v.getClass().getDeclaredMethods();
            GXPermissionCtl permissionCtl = v.getClass().getAnnotation(GXPermissionCtl.class);
            String moduleCode = permissionCtl.moduleCode();
            String moduleName = permissionCtl.moduleName();
            List<GXBasePermissionInnerDto> permissionDtoList = new ArrayList<>();
            for (Method declaredMethod : declaredMethods) {
                GXPermission permissionAction = declaredMethod.getAnnotation(GXPermission.class);
                if (Objects.nonNull(permissionAction)) {
                    String permissionModuleCode = permissionAction.moduleCode();
                    String permissionModuleName = permissionAction.moduleName();
                    if (CharSequenceUtil.isEmpty(permissionModuleCode)) {
                        permissionModuleCode = moduleCode;
                    }
                    if (CharSequenceUtil.isEmpty(permissionModuleName)) {
                        permissionModuleName = moduleName;
                    }
                    GXBasePermissionInnerDto permissionDto = GXBasePermissionInnerDto.builder()
                            .permissionCode(permissionAction.permissionCode())
                            .permissionName(permissionAction.permissionName())
                            .moduleName(permissionModuleName)
                            .moduleCode(permissionModuleCode)
                            .build();
                    permissionDtoList.add(permissionDto);
                }
            }
            concurrentHashMap.putIfAbsent(k, permissionDtoList);
        });
        GXPermissionEvent<Map<String, List<GXBasePermissionInnerDto>>> permissionEvent = new GXPermissionEvent<>(concurrentHashMap, Dict.create());
        GXEventPublisherUtils.publishEvent(permissionEvent);
    }
}
