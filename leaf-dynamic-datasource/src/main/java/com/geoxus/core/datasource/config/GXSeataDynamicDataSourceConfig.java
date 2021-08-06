package com.geoxus.core.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置多数据源
 */
@Configuration
@ConditionalOnClass(value = {DruidDataSource.class, GXDynamicDataSourceConfig.class}, name = {"io.seata.rm.datasource.DataSourceProxy"})
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class GXSeataDynamicDataSourceConfig {
    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(GXDynamicDataSource dynamicDataSource) {
        // 这里用MybatisSqlSessionFactoryBean代替了SqlSessionFactoryBean, 否则MyBatisPlus不会生效
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dynamicDataSource);
        return mybatisSqlSessionFactoryBean;
    }
}