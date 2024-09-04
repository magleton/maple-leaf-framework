package cn.maple.core.framework.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.exception.GXCorsConfigException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * 处理跨域问题
 *
 * @author britton chen <britton@126.com>
 */
@Configuration
public class GXCORSConfig {
    @Value("${cors.allow.credentials:false}")
    private boolean allowCredentials;

    @Value("${cors.allow.origins:*}")
    private String allowOrigins;

    @Value("${cors.allow.methods:*}")
    private String allowMethods;

    @Value("${cors.allow.headers:*}")
    private String allowHeaders;

    /**
     * 注意:
     * 如果 corsConfiguration.setAllowCredentials(true);
     * 则需要将corsConfiguration.addAllowedOrigin("*");
     * 改成 corsConfiguration.addAllowedOriginPattern("*");
     *
     * @return CorsConfiguration
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        if (allowCredentials && CharSequenceUtil.equals(allowOrigins, "*")) {
            throw new GXCorsConfigException("Access-Control-Allow-Origin不能设置为*,否则cookie不会出现在http的请求头里!!", HttpStatus.HTTP_INTERNAL_ERROR);
        }
        corsConfiguration.setAllowCredentials(allowCredentials);
        corsConfiguration.setAllowedOrigins(Arrays.asList(allowOrigins.split(",")));
        corsConfiguration.setAllowedMethods(Arrays.asList(allowMethods.split(",")));
        corsConfiguration.setAllowedHeaders(Arrays.asList(allowHeaders.split(",")));
        List<String> exposedHeaders = CollUtil.newArrayList("Access-Control-ALLOW-HEADERS", "Access-Control-Expose-Headers", "Access-Control-ALLOW-ORIGIN");
        if (allowCredentials) {
            exposedHeaders.add("Access-Control-Allow-Credentials");
        }
        corsConfiguration.setExposedHeaders(exposedHeaders);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
