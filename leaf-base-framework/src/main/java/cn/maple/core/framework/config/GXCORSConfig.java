package cn.maple.core.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 处理跨域问题
 *
 * @author britton chen <britton@126.com>
 */
@Configuration
public class GXCORSConfig {
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
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setExposedHeaders(Arrays.asList("Access-Control-ALLOW-HEADERS", "Access-Control-Expose-Headers", "Access-Control-ALLOW-ORIGIN"));
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
