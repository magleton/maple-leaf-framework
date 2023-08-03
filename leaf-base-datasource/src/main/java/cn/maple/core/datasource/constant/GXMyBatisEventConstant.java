package cn.maple.core.datasource.constant;

public class GXMyBatisEventConstant {
    /**
     * 发送的Spring事件使用同步监听器监听
     */
    public static final String MYBATIS_SYNC_EVENT = "sync";

    /**
     * 发送的Spring事件使用同步异步器监听
     */
    public static final String MYBATIS_ASYNC_EVENT = "async";

    private GXMyBatisEventConstant() {
    }
}
