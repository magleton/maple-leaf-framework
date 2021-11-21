package cn.maple.core.framework.listener;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.annotation.GXMenu;
import cn.maple.core.framework.annotation.GXMenuPermission;
import cn.maple.core.framework.dto.menu.GXMenuDto;
import cn.maple.core.framework.dto.menu.GXMenuPermissionDto;
import cn.maple.core.framework.event.GXMenuPermissionEvent;
import cn.maple.core.framework.util.GXCommonUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile({"dev", "local", "test"})
public class GXApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        ConcurrentHashMap<String, GXMenuDto> concurrentHashMap = new ConcurrentHashMap<>();
        Map<String, Object> beansWithAnnotation = applicationStartedEvent.getApplicationContext().getBeanFactory().getBeansWithAnnotation(GXMenu.class);
        beansWithAnnotation.forEach((k, v) -> {
            Method[] declaredMethods = v.getClass().getDeclaredMethods();
            GXMenu permissionCtl = v.getClass().getAnnotation(GXMenu.class);
            String menuName = permissionCtl.menuName();
            String menuUrl = permissionCtl.menuUrl();
            String menuCode = permissionCtl.menuCode();
            int menuType = permissionCtl.menuType();
            ArrayList<GXMenuPermissionDto> permissionDtoList = new ArrayList<>();
            for (Method declaredMethod : declaredMethods) {
                GXMenuPermission permissionAction = declaredMethod.getAnnotation(GXMenuPermission.class);
                if (Objects.nonNull(permissionAction)) {
                    GXMenuPermissionDto permissionDto = GXMenuPermissionDto.builder()
                            .permissionCode(CharSequenceUtil.format("{}:{}", menuCode, permissionAction.permissionCode()))
                            .menuGroup(permissionAction.menuGroup())
                            .menuType(permissionAction.menuType())
                            .permissionName(permissionAction.permissionName())
                            .menuUrl(CharSequenceUtil.format("{}/{}", menuUrl, permissionAction.menuUrl()))
                            .menuName(permissionAction.menuName())
                            .controllerName(v.getClass().getName())
                            .actionName(declaredMethod.getName())
                            .build();
                    permissionDtoList.add(permissionDto);
                }
            }
            GXMenuDto menuDto = GXMenuDto.builder()
                    .menuCode(menuCode)
                    .menuUrl(menuUrl)
                    .menuType(menuType)
                    .menuName(menuName)
                    .permissionDtoList(permissionDtoList)
                    .build();
            concurrentHashMap.putIfAbsent(k, menuDto);
            GXMenuPermissionEvent<Map<String, GXMenuDto>> permissionEvent = new GXMenuPermissionEvent<>(concurrentHashMap, Dict.create());
            GXCommonUtils.publishEvent(permissionEvent);
        });
    }
}
