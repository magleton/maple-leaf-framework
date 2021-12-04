package cn.maple.core.framework.listener;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.annotation.GXPermission;
import cn.maple.core.framework.dto.permissions.GXPermissionDto;
import cn.maple.core.framework.event.GXPermissionEvent;
import cn.maple.core.framework.util.GXCommonUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

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
        ConcurrentHashMap<String, List<GXPermissionDto>> concurrentHashMap = new ConcurrentHashMap<>();
        Map<String, Object> beansWithAnnotation = applicationStartedEvent.getApplicationContext().getBeanFactory().getBeansWithAnnotation(RestController.class);
        beansWithAnnotation.forEach((k, v) -> {
            Method[] declaredMethods = v.getClass().getDeclaredMethods();
            List<GXPermissionDto> permissionDtoList = new ArrayList<>();
            for (Method declaredMethod : declaredMethods) {
                GXPermission permissionAction = declaredMethod.getAnnotation(GXPermission.class);
                if (Objects.nonNull(permissionAction)) {
                    GXPermissionDto permissionDto = GXPermissionDto.builder()
                            .permissionCode(permissionAction.permissionCode())
                            .permissionName(permissionAction.permissionName())
                            .permissionModule(permissionAction.permissionModule())
                            .build();
                    permissionDtoList.add(permissionDto);
                }
            }
            concurrentHashMap.putIfAbsent(k, permissionDtoList);
        });
        GXPermissionEvent<Map<String, List<GXPermissionDto>>> permissionEvent = new GXPermissionEvent<>(concurrentHashMap, Dict.create());
        GXCommonUtils.publishEvent(permissionEvent);
    }
}
