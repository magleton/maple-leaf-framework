package cn.maple.sentinel.config;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.nacos.api.PropertyKeyConst;

import java.util.List;
import java.util.Properties;

public class GXNacosDataSourceInitFunc implements InitFunc {
    /**
     * 流控规则在nacos中的配置后缀
     */
    private static final String POST_FLOW_PREFIX = "-flow-rules";

    /**
     * 从当前项目的配置文件中获取nacos的命名空间
     *
     * @return String
     * @author britton
     */
    private static String getNamespace() {
        return GXCommonUtils.getEnvironmentValue("nacos.config.namespace", String.class);
    }

    /**
     * 从当前项目的配置文件中获取nacos的主机地址
     *
     * @return String
     * @author britton
     */
    private static String getNacosServerAddress() {
        return GXCommonUtils.getEnvironmentValue("nacos.config.server-addr", String.class);
    }

    /**
     * 从当前项目的配置文件中获取nacos的group配置
     *
     * @return String
     * @author britton
     */
    private static String getNacosGroupId() {
        return GXCommonUtils.getEnvironmentValue("nacos.config.group", String.class, "DEFAULT_GROUP");
    }

    /**
     * 从当前项目的配置文件中获取nacos的dataId配置
     *
     * @return String
     * @author britton
     */
    private static String getNacosDataId() {
        return GXCommonUtils.getEnvironmentValue("spring.application.name", String.class, "default") + POST_FLOW_PREFIX;
    }

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, getNacosServerAddress());
        properties.put(PropertyKeyConst.NAMESPACE, getNamespace());
        String nacosGroupId = getNacosGroupId();
        String nacosDataId = getNacosDataId();
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(properties, nacosGroupId, nacosDataId,
                source -> JSONUtil.toBean(source, new TypeReference<>() {
                }, true));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
