package cn.maple.core.datasource.config;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.datasource.annotation.GXDataSource;
import cn.maple.core.datasource.properties.GXDataSourceProperties;
import cn.maple.core.datasource.properties.GXDynamicDataSourceProperties;
import cn.maple.core.framework.config.aware.GXApplicationContextAware;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.alibaba.druid.pool.DruidDataSource;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 配置多数据源
 */
@Configuration
@Slf4j
public class GXDynamicDataSourceConfig extends GXApplicationContextAware {
    @Resource
    private GXDynamicDataSourceProperties dynamicDataSourceProperties;

    @Bean
    public GXDataSourceProperties dataSourceProperties() {
        GXDataSourceProperties dataSourceProperties = new GXDataSourceProperties();
        if (!CharSequenceUtil.isEmpty(dataSourceProperties.getUrl())) {
            return dataSourceProperties;
        }
        String[] defaultDataSourceName = new String[]{"framework"};
        GXSpringContextUtils.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class).forEach((name, obj) -> {
            GXDataSource annotation = obj.getClass().getAnnotation(GXDataSource.class);
            if (Objects.nonNull(annotation)) {
                defaultDataSourceName[0] = annotation.value();
            }
        });
        return dynamicDataSourceProperties.getDatasource().get(defaultDataSourceName[0]);
    }

    @Bean
    public GXDynamicDataSource dynamicDataSource() {
        GXDynamicDataSource dynamicDataSource = new GXDynamicDataSource();
        Map<Object, Object> dynamicDataSources = getDynamicDataSources();
        dynamicDataSource.setTargetDataSources(dynamicDataSources);
        String[] defaultDataSourceName = new String[]{"framework"};
        GXSpringContextUtils.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class).forEach((name, obj) -> {
            GXDataSource annotation = obj.getClass().getAnnotation(GXDataSource.class);
            if (Objects.nonNull(annotation)) {
                defaultDataSourceName[0] = annotation.value();
            }
        });
        Optional<Object> firstDataSource = dynamicDataSources.keySet().stream().filter(name -> name.equals(defaultDataSourceName[0])).findFirst();
        if (firstDataSource.isEmpty()) {
            throw new GXBusinessException("请配置正确的数据源");
        }
        // 默认数据源
        DataSource defaultDataSource = (DataSource) dynamicDataSources.get(firstDataSource.get());
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        return dynamicDataSource;
    }

    /**
     * 将普通的DataSource对象包装成Seata的DataSourceProxy
     *
     * @param dataSource 数据源
     * @return DataSource
     * @author britton <britton@126.com>
     * @since 2021-02-24
     */
    private DataSource wrapSeataDataSource(DataSource dataSource) {
        try {
            Class<?> forName = Class.forName("io.seata.rm.datasource.DataSourceProxy");
            Constructor<?> constructor = ReflectUtil.getConstructor(forName, DataSource.class);
            if (constructor == null) {
                return dataSource;
            }
            Object o = ReflectUtil.newInstance(forName, dataSource);
            if (o == null) {
                return dataSource;
            }
            return (DataSource) o;
        } catch (ClassNotFoundException e) {
            log.debug("未找到Seata的DataSourceProxy类,将使用原始的DataSource对象!!!!");
        }
        return dataSource;
    }

    protected Map<Object, Object> getDynamicDataSources() {
        Map<String, GXDataSourceProperties> dataSourcePropertiesMap = dynamicDataSourceProperties.getDatasource();
        Map<Object, Object> targetDataSources = new LinkedHashMap<>(dataSourcePropertiesMap.size());
        // TODO 此处可以通过在其他地方获取连接信息来新建连接池, 比如从另外的数据库读取信息
        dataSourcePropertiesMap.forEach((k, v) -> {
            DruidDataSource druidDataSource = GXDynamicDataSourceFactory.buildDruidDataSource(v);
            DataSource dataSource = wrapSeataDataSource(druidDataSource);
            targetDataSources.put(k, dataSource);
        });
        return targetDataSources;
    }
}
