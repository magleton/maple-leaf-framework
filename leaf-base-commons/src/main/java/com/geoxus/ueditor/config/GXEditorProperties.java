package com.geoxus.ueditor.config;

import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 百度编辑器配置
 */
@Component
@Data
@ConfigurationProperties(prefix = "ue")
@PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/ueditor.yml"},
        factory = GXYamlPropertySourceFactory.class,
        encoding = "utf-8",
        ignoreResourceNotFound = true)
public class GXEditorProperties {
    /**
     * config.json 路径
     */
    private String configFile;

    /**
     * UEditor服务器统一请求接口路径
     */
    private String serverUrl;

    private QiNiu qiniu;

    private Local local;

    /**
     * 上传到本地参数
     */
    @Data
    public static class Local {
        /**
         * 资源访问前缀
         */
        private String urlPrefix;
        /**
         * 存储文件的绝对路径 必须使用标准路径"/"作为分隔符
         * 默认为"/"即当前项目所在磁盘根目录
         */
        private String physicalPath = "/";
    }

    /**
     * 七牛上传参数
     */
    @Data
    public static class QiNiu {
        private String accessKey;
        private String secretKey;
        private String cdn;
        private String bucket;
        private String zone;
    }
}
