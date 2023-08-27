package cn.maple.elasticsearch.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXElasticsearchProperties extends ElasticsearchProperties {
    /**
     * 是否为主要的连接
     */
    private boolean primary = false;
}
