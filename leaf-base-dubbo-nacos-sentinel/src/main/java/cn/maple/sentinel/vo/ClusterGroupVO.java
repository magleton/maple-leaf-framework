package cn.maple.sentinel.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
public class ClusterGroupVO {
    /**
     * 机器ID
     */
    private String machineId;

    /**
     * IP
     */
    private String ip;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 客户端集合
     */
    private Set<String> clientSet;
}