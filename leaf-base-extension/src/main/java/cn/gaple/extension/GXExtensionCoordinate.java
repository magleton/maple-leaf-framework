package cn.gaple.extension;

/**
 * Extension Coordinate(扩展坐标) is used to uniquely position an Extension
 *
 * @author britton
 */
public class GXExtensionCoordinate {
    /**
     * 扩展点名字
     */
    private final String extensionPointName;

    /**
     * 业务场景唯一标识
     */
    private final String bizScenarioUniqueIdentity;

    /**
     * 扩展点的类型
     */
    private Class<?> extensionPointClass;

    /**
     * 业务场景
     */
    private GXBizScenario bizScenario;

    /**
     * @param extPtClass  扩展点类型
     * @param bizScenario 业务场景
     */
    public GXExtensionCoordinate(Class<?> extPtClass, GXBizScenario bizScenario) {
        this.extensionPointClass = extPtClass;
        this.extensionPointName = extPtClass.getName();
        this.bizScenario = bizScenario;
        this.bizScenarioUniqueIdentity = bizScenario.getUniqueIdentity();
    }

    /**
     * @param extensionPoint 扩展点名字
     * @param bizScenario    业务场景
     */
    public GXExtensionCoordinate(String extensionPoint, String bizScenario) {
        this.extensionPointName = extensionPoint;
        this.bizScenarioUniqueIdentity = bizScenario;
    }

    /**
     * 扩展坐标的值
     *
     * @param extPtClass  扩展点的类型
     * @param bizScenario 业务场景值
     * @return GXExtensionCoordinate
     */
    public static GXExtensionCoordinate valueOf(Class<?> extPtClass, GXBizScenario bizScenario) {
        return new GXExtensionCoordinate(extPtClass, bizScenario);
    }

    /**
     * 获取扩展点的类型
     *
     * @param <T> 扩展点类型
     * @return Class
     */
    public <T> Class<T> getExtensionPointClass() {
        return (Class<T>) extensionPointClass;
    }

    public GXBizScenario getBizScenario() {
        return bizScenario;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bizScenarioUniqueIdentity == null) ? 0 : bizScenarioUniqueIdentity.hashCode());
        result = prime * result + ((extensionPointName == null) ? 0 : extensionPointName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GXExtensionCoordinate other = (GXExtensionCoordinate) obj;
        if (bizScenarioUniqueIdentity == null) {
            if (other.bizScenarioUniqueIdentity != null) return false;
        } else if (!bizScenarioUniqueIdentity.equals(other.bizScenarioUniqueIdentity)) {
            return false;
        }
        if (extensionPointName == null) {
            return other.extensionPointName == null;
        } else {
            return extensionPointName.equals(other.extensionPointName);
        }
    }

    @Override
    public String toString() {
        return "ExtensionCoordinate [extensionPointName=" + extensionPointName + ", bizScenarioUniqueIdentity=" + bizScenarioUniqueIdentity + "]";
    }
}
