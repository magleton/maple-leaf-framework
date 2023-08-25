package cn.maple.elasticsearch.properties;

import lombok.Data;

import java.util.List;

@Data
public class GXElasticsearchProperties {
    /**
     * 连接地址
     */
    private List<String> uris;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否为主要的连接
     */
    private boolean primary = false;
}
