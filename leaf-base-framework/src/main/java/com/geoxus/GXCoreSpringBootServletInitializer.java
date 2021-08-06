package com.geoxus;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 该类用于打包war文件，方便部署到应用容器中，比如:Tomcat...
 */
public class GXCoreSpringBootServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GXCoreApplication.class);
    }
}
