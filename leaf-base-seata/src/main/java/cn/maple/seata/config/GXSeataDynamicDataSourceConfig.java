package cn.maple.seata.config;

import cn.maple.core.datasource.config.GXDynamicDataSource;
import cn.maple.core.datasource.config.GXDynamicDataSourceConfig;
import cn.maple.core.datasource.handler.GXAutoFillMetaObjectHandler;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import jakarta.annotation.Resource;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
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
    @Resource
    private GXAutoFillMetaObjectHandler autoFillMetaObjectHandler;

    @Resource
    private MybatisPlusProperties mybatisPlusProperties;

    @Resource
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(GXDynamicDataSource dynamicDataSource) {
        // 这里用MybatisSqlSessionFactoryBean代替了SqlSessionFactoryBean, 否则MyBatisPlus不会生效
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dynamicDataSource);
        mybatisSqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        mybatisSqlSessionFactoryBean.setConfigurationProperties(mybatisPlusProperties.getConfigurationProperties());
        // MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 向代理数据源添加分页拦截器
        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        mybatisSqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor);
        // 设置mapper文件的位置
        mybatisSqlSessionFactoryBean.setMapperLocations(mybatisPlusProperties.resolveMapperLocations());
        GlobalConfig globalConfig = mybatisPlusProperties.getGlobalConfig();
        // 设置字段自动填充
        globalConfig.setMetaObjectHandler(autoFillMetaObjectHandler);
        // 代理数据源添加id生成器
        /*globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());*/
        mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig);
        return mybatisSqlSessionFactoryBean;
    }
}