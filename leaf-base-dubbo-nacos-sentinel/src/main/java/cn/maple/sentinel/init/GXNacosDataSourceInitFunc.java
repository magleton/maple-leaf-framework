package cn.maple.sentinel.init;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.sentinel.vo.ClusterGroupVO;
import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.nacos.api.PropertyKeyConst;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * @author briotton
 */
public class GXNacosDataSourceInitFunc implements InitFunc {
    /**
     * 流控规则配置文件后缀
     */
    private static final String FLOW_POSTFIX = "-flow-rules";

    /**
     * 参数规则配置文件后缀
     */
    private static final String PARAM_FLOW_POSTFIX = "-param-rules";

    /**
     * 集群客户端规则配置文件后缀
     */
    private static final String CONFIG_POSTFIX = "-cluster-client-config";

    /**
     * 集群规则配置文件后缀
     */
    private static final String CLUSTER_MAP_POSTFIX = "-cluster-map";

    /**
     * 应用程序名字在配置文件中的KEY
     */
    private static final String APPLICATION_NAME_KEY = "spring.application.name";

    /**
     * 机器ID的分隔符
     */
    private static final String SEPARATOR = "@";

    @Override
    public void init() throws Exception {
        // Register client dynamic rule data source.
        initDynamicRuleProperty();

        // Register token client related data source.
        // Token client common config:
        initClientConfigProperty();
        // Token client assign config (e.g. target token server) retrieved from assign map:
        initClientServerAssignProperty();

        // Register token server related data source.
        // Register dynamic rule data source supplier for token server:
        registerClusterRuleSupplier();
        // Token server transport config extracted from assign map:
        initServerTransportConfigProperty();

        // Init cluster state property for extracting mode from cluster map data source.
        initStateProperty();
    }

    /**
     * 初始化动态规则
     *
     * @author britton
     */
    private void initDynamicRuleProperty() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, getNacosServerAddress());
        properties.put(PropertyKeyConst.NAMESPACE, getNamespace());
        ReadableDataSource<String, List<FlowRule>> ruleSource = new NacosDataSource<>(properties,
                getNacosGroupId(),
                getFlowDataId(),
                source -> JSONUtil.toBean(source, new TypeReference<>() {
                }, true)
        );
        FlowRuleManager.register2Property(ruleSource.getProperty());
        ReadableDataSource<String, List<ParamFlowRule>> paramRuleSource = new NacosDataSource<>(properties,
                getNacosGroupId(),
                getParamDataId(),
                source -> JSONUtil.toBean(source, new TypeReference<>() {
                }, true)
        );
        ParamFlowRuleManager.register2Property(paramRuleSource.getProperty());
    }

    /**
     * 初始化客户端配置
     *
     * @author britton
     */
    private void initClientConfigProperty() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, getNacosServerAddress());
        properties.put(PropertyKeyConst.NAMESPACE, getNamespace());
        ReadableDataSource<String, ClusterClientConfig> clientConfigDs = new NacosDataSource<>(properties,
                getNacosGroupId(),
                getConfigDataId(),
                source -> JSONUtil.toBean(source, new TypeReference<>() {
                }, true)
        );
        ClusterClientConfigManager.registerClientConfigProperty(clientConfigDs.getProperty());
    }

    /**
     * 初始化服务器传输配置
     *
     * @author britton
     */
    private void initServerTransportConfigProperty() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, getNacosServerAddress());
        properties.put(PropertyKeyConst.NAMESPACE, getNamespace());
        ReadableDataSource<String, ServerTransportConfig> serverTransportDs = new NacosDataSource<>(properties,
                getNacosGroupId(),
                getClusterMapDataId(),
                source -> {
                    List<ClusterGroupVO> groupList = JSONUtil.toBean(source, new TypeReference<>() {
                    }, true);
                    return Optional.ofNullable(groupList)
                            .flatMap(this::extractServerTransportConfig)
                            .orElse(null);
                }
        );
        ClusterServerConfigManager.registerServerTransportProperty(serverTransportDs.getProperty());
    }

    /**
     * 注册集群规则提供者
     * Register cluster flow rule property supplier which creates data source by namespace.
     * Flow rule dataId format: ${namespace}-flow-rules
     *
     * @author britton
     */
    private void registerClusterRuleSupplier() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, getNacosServerAddress());
        properties.put(PropertyKeyConst.NAMESPACE, getNamespace());
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<FlowRule>> ds = new NacosDataSource<>(properties,
                    getNacosGroupId(),
                    namespace + FLOW_POSTFIX,
                    source -> JSONUtil.toBean(source, new TypeReference<>() {
                    }, true)
            );
            return ds.getProperty();
        });
        // Register cluster parameter flow rule property supplier which creates data source by namespace.
        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<ParamFlowRule>> ds = new NacosDataSource<>(properties,
                    getNacosGroupId(),
                    namespace + PARAM_FLOW_POSTFIX,
                    source -> JSONUtil.toBean(source, new TypeReference<>() {
                    }, true)
            );
            return ds.getProperty();
        });
    }

    /**
     * 初始化客户端服务分配属性
     * Cluster map format:
     * [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
     * machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
     *
     * @author britton
     */
    private void initClientServerAssignProperty() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, getNacosServerAddress());
        properties.put(PropertyKeyConst.NAMESPACE, getNamespace());
        ReadableDataSource<String, ClusterClientAssignConfig> clientAssignDs = new NacosDataSource<>(properties,
                getNacosGroupId(),
                getClusterMapDataId(),
                source -> {
                    List<ClusterGroupVO> groupList = JSONUtil.toBean(source, new TypeReference<>() {
                    }, true);
                    return Optional.ofNullable(groupList)
                            .flatMap(this::extractClientAssignment)
                            .orElse(null);
                }
        );
        ClusterClientConfigManager.registerServerAssignProperty(clientAssignDs.getProperty());
    }

    /**
     * 初始化状态属性
     * Cluster map format:
     * [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
     * machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
     *
     * @author britton
     */
    private void initStateProperty() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, getNacosServerAddress());
        properties.put(PropertyKeyConst.NAMESPACE, getNamespace());
        ReadableDataSource<String, Integer> clusterModeDs = new NacosDataSource<>(properties,
                getNacosGroupId(),
                getClusterMapDataId(),
                source -> {
                    List<ClusterGroupVO> groupList = JSONUtil.toBean(source, new TypeReference<>() {
                    }, true);
                    return Optional.ofNullable(groupList)
                            .map(this::extractMode)
                            .orElse(ClusterStateManager.CLUSTER_NOT_STARTED);
                }
        );
        ClusterStateManager.registerProperty(clusterModeDs.getProperty());
    }

    /**
     * 抽取模式
     *
     * @param groupList 组列表
     * @return int
     */
    private int extractMode(List<ClusterGroupVO> groupList) {
        // If any server group machineId matches current, then it's token server.
        if (groupList.stream().anyMatch(this::machineEqual)) {
            return ClusterStateManager.CLUSTER_SERVER;
        }
        // If current machine belongs to any of the token server group, then it's token client.
        // Otherwise it's unassigned, should be set to NOT_STARTED.
        boolean canBeClient = groupList.stream()
                .flatMap(e -> e.getClientSet().stream())
                .filter(Objects::nonNull)
                .anyMatch(e -> e.equals(getCurrentMachineId()));
        return canBeClient ? ClusterStateManager.CLUSTER_CLIENT : ClusterStateManager.CLUSTER_NOT_STARTED;
    }

    /**
     * 抽取服务端传输配置
     *
     * @param groupList 组列表
     * @return ServerTransportConfig
     */
    private Optional<ServerTransportConfig> extractServerTransportConfig(List<ClusterGroupVO> groupList) {
        return groupList.stream()
                .filter(this::machineEqual)
                .findAny()
                .map(e -> new ServerTransportConfig().setPort(e.getPort()).setIdleSeconds(600));
    }

    /**
     * 抽取客户端分配
     *
     * @param groupList 组列表
     * @return ClusterClientAssignConfig
     */
    private Optional<ClusterClientAssignConfig> extractClientAssignment(List<ClusterGroupVO> groupList) {
        if (groupList.stream().anyMatch(this::machineEqual)) {
            return Optional.empty();
        }
        // Build client assign config from the client set of target server group.
        for (ClusterGroupVO group : groupList) {
            if (group.getClientSet().contains(getCurrentMachineId())) {
                String ip = group.getIp();
                Integer port = group.getPort();
                return Optional.of(new ClusterClientAssignConfig(ip, port));
            }
        }
        return Optional.empty();
    }

    /**
     * 判断机器是否相等
     *
     * @param group 组对象
     * @return boolean
     */
    private boolean machineEqual(/*@Valid*/ ClusterGroupVO group) {
        return getCurrentMachineId().equals(group.getMachineId());
    }

    /**
     * 获取当前的机器ID
     * Note: this may not work well for container-based env.
     *
     * @return 机器ID
     */
    private String getCurrentMachineId() {
        return HostNameUtil.getIp() + SEPARATOR + TransportConfig.getRuntimePort();
    }

    /**
     * 获取流控的nacos配置信息
     *
     * @return String
     */
    private String getFlowDataId() {
        return GXCommonUtils.getEnvironmentValue(APPLICATION_NAME_KEY, String.class) + FLOW_POSTFIX;
    }

    /**
     * 获取参数的nacos配置信息
     *
     * @return String
     */
    private String getParamDataId() {
        return GXCommonUtils.getEnvironmentValue(APPLICATION_NAME_KEY, String.class) + PARAM_FLOW_POSTFIX;
    }

    /**
     * 获取config的nacos配置信息
     *
     * @return String
     */
    private String getConfigDataId() {
        return GXCommonUtils.getEnvironmentValue(APPLICATION_NAME_KEY, String.class) + CONFIG_POSTFIX;
    }

    /**
     * 获取ClusterMap的nacos配置信息
     * {@code
     * Cluster map format:
     * [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
     * machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
     * }
     *
     * @return String
     */
    private String getClusterMapDataId() {
        return GXCommonUtils.getEnvironmentValue(APPLICATION_NAME_KEY, String.class) + CLUSTER_MAP_POSTFIX;
    }

    /**
     * 从当前项目的配置文件中获取nacos的主机地址
     *
     * @return String
     * @author britton
     */
    private String getNacosServerAddress() {
        return GXCommonUtils.getEnvironmentValue("nacos.config.server-addr", String.class);
    }

    /**
     * 从当前项目的配置文件中获取nacos的group配置
     *
     * @return String
     * @author britton
     */
    private String getNacosGroupId() {
        return GXCommonUtils.getEnvironmentValue("nacos.config.group", String.class, "DEFAULT_GROUP");
    }

    /**
     * 从当前项目的配置文件中获取nacos的命名空间
     *
     * @return String
     * @author britton
     */
    private String getNamespace() {
        return GXCommonUtils.getEnvironmentValue("nacos.config.namespace", String.class);
    }
}
