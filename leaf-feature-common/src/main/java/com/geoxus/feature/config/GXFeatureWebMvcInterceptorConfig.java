package com.geoxus.feature.config;

import com.geoxus.core.framework.properties.web.GXWebMvcProperties;
import com.geoxus.core.framework.util.GXSpringContextUtil;
import com.geoxus.core.framework.web.interceptor.GXWebMvcInterceptor;
import com.geoxus.feature.properties.GXUploadProperties;
import com.geoxus.ueditor.config.GXEditorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class GXFeatureWebMvcInterceptorConfig extends GXWebMvcInterceptor {
    @Resource
    private GXWebMvcProperties webMvcProperties;

    /**
     * 对文件的路径进行配置,创建一个虚拟路径/Path/** ，即只要在<img src="/Path/picName.jpg"/>便可以直接引用图片
     * 这是图片的物理路径  "file:/+本地图片的地址"
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        try {
            final String resourceLocationsPrefix = "file:";
            final List<String> list = webMvcProperties.getResourcePatterns();
            String[] array = new String[list.size()];
            list.toArray(array);
            if (Objects.nonNull(GXSpringContextUtil.getBean(GXUploadProperties.class))) {
                GXUploadProperties uploadProperties = GXSpringContextUtil.getBean(GXUploadProperties.class);
                registry.addResourceHandler(array)
                        .addResourceLocations(resourceLocationsPrefix + ResourceUtils.getURL(uploadProperties.getDepositPath()).getPath())
                        //PHP图片地址
                        .addResourceLocations(resourceLocationsPrefix + ResourceUtils.getURL(uploadProperties.getDepositPath() + File.separator + "media").getPath());
            }
            //编辑器上传路径
            if (Objects.nonNull(GXSpringContextUtil.getBean(GXEditorProperties.class))) {
                GXEditorProperties editorProperties = GXSpringContextUtil.getBean(GXEditorProperties.class);
                registry.addResourceHandler(editorProperties.getLocal().getUrlPrefix() + "**")
                        .addResourceLocations(resourceLocationsPrefix + ResourceUtils.getURL(editorProperties.getLocal().getPhysicalPath()).getPath())
                        .setCacheControl(CacheControl.maxAge(20, TimeUnit.DAYS));
            }
        } catch (Exception e) {
            log.error("虚拟路径配置错误", e);
        }
    }
}
