package com.geoxus.commons.config;

import com.geoxus.core.common.config.GXWebMvcConfig;
import com.geoxus.core.common.config.GXWebMvcInterceptorConfig;
import com.geoxus.ueditor.config.GXEditorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class GXCommonsWebMvcInterceptorConfig extends GXWebMvcInterceptorConfig {
    @Autowired
    private GXUploadConfig gxUploadConfig;

    @Autowired
    private GXWebMvcConfig gxWebMvcConfig;

    @Autowired
    private GXEditorProperties properties;

    /**
     * 对文件的路径进行配置,创建一个虚拟路径/Path/** ，即只要在<img src="/Path/picName.jpg"/>便可以直接引用图片
     * 这是图片的物理路径  "file:/+本地图片的地址"
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        try {
            final String resourceLocationsPrefix = "file:";
            final List<String> list = gxWebMvcConfig.getResourcePatterns();
            String[] array = new String[list.size()];
            list.toArray(array);
            registry.addResourceHandler(array)
                    .addResourceLocations(resourceLocationsPrefix + ResourceUtils.getURL(gxUploadConfig.getDepositPath()).getPath())
                    //PHP图片地址
                    .addResourceLocations(resourceLocationsPrefix + ResourceUtils.getURL(gxUploadConfig.getDepositPath() + File.separator + "media").getPath());
            //编辑器上传路径
            registry.addResourceHandler(properties.getLocal().getUrlPrefix() + "**")
                    .addResourceLocations(resourceLocationsPrefix + ResourceUtils.getURL(properties.getLocal().getPhysicalPath()).getPath())
                    .setCacheControl(CacheControl.maxAge(20, TimeUnit.DAYS));
        } catch (Exception e) {
            log.error("虚拟路径配置错误", e);
        }
    }
}
