package cn.maple.extension;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExtensionRepository 扩展仓库 保存扩展信息
 *
 * @author britton
 */
@Component
public class GXExtensionRepository {
    /**
     * 扩展点缓存对象
     */
    private final Map<GXExtensionCoordinate, GXExtensionPoint> extensionRepo = new ConcurrentHashMap<>();

    /**
     * 获取扩展点缓存对象
     *
     * @return Map
     */
    public Map<GXExtensionCoordinate, GXExtensionPoint> getExtensionRepo() {
        return extensionRepo;
    }
}
