package cn.maple.core.datasource.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.service.GXDBSchemaService;
import cn.maple.core.datasource.service.GXTenantIdService;
import cn.maple.core.framework.config.aware.GXApplicationContextSingleton;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.handlers.MybatisMapWrapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@EnableTransactionManagement
@Configuration
public class GXMyBatisPlusConfig {
    @Resource
    private ApplicationContext applicationContext;

    private static void customize(org.apache.ibatis.session.Configuration configuration) {
        configuration.setObjectWrapperFactory(new ObjectWrapperFactory() {
            @Override
            public boolean hasWrapperFor(Object object) {
                return object instanceof Map;
            }

            @Override
            public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
                final Map<String, Object> map = Convert.convert(new TypeReference<>() {
                }, object);
                return new MybatisMapWrapper(metaObject, map) {
                    @Override
                    public String findProperty(String name, boolean useCamelCaseMapping) {
                        if (useCamelCaseMapping && !StringUtils.isCamel(name)) {
                            return StringUtils.underlineToCamel(name);
                        }
                        return name;
                    }
                };
            }
        });
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 将ApplicationContext提前注入
        if (GXApplicationContextSingleton.INSTANCE.getApplicationContext() == null) {
            GXApplicationContextSingleton.INSTANCE.setApplicationContext(applicationContext);
        }
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setOptimizeJoin(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // sql性能规范插件
        List<String> activeProfiles = Convert.toList(String.class, applicationContext.getEnvironment().getActiveProfiles());
        // 除去正式环境,其他环境都开启sql性能规范插件
        if (!CollUtil.contains(activeProfiles, GXCommonConstant.RUN_ENV_PROD)) {
            interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }
        // 多租户插件(请在相应的表中新增tenant_id字段)
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new GXTenantLineHandler()));
        // interceptor.addInnerInterceptor(new TenantLineInnerInterceptor());
        // 动态表名插件
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        /* mybatis-plus-boot-starter  3.4.3.3 以下的版本需要如下配置*/
        Map<String, TableNameHandler> tableNameHandlerMap = new HashMap<>();
        tableNameHandlerMap.put("tableName", (sql, tableName) -> tableName);
        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(tableNameHandlerMap);
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        return interceptor;
    }

    @Bean
    @ConditionalOnExpression("'${use-camel-case-mapping}'.equals('true')")
    public ConfigurationCustomizer configurationCustomizer() {
        return GXMyBatisPlusConfig::customize;
    }

    /**
     * 自定义多租户插件类
     *
     * @author britton
     * @since 2021-11-17
     */
    private static class GXTenantLineHandler implements TenantLineHandler {
        /**
         * 数据缓存管理器
         */
        private static final CaffeineCacheManager caffeineCacheManager;

        /**
         * DB的Schema服务类
         */
        private static final GXDBSchemaService dbSchemaService;

        static {
            caffeineCacheManager = GXSpringContextUtils.getBean(CaffeineCacheManager.class);
            dbSchemaService = GXSpringContextUtils.getBean(GXDBSchemaService.class);
        }

        /**
         * 获取租户 ID 值表达式，只支持单个 ID 值
         * <p>
         *
         * @return 租户 ID 值表达式
         */
        @Override
        public Expression getTenantId() {
            GXTenantIdService tenantIdService = GXSpringContextUtils.getBean(GXTenantIdService.class);
            assert tenantIdService != null;
            return tenantIdService.getTenantId();
        }

        /**
         * 获取租户字段名
         * <p>
         * 默认字段名叫: tenant_id
         *
         * @return 租户字段名
         */
        @Override
        public String getTenantIdColumn() {
            return "tenant_id";
        }

        /**
         * 根据表名判断是否忽略拼接多租户条件
         * <p>
         * 默认都要进行解析并拼接多租户条件
         *
         * @param tableName 表名
         * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
         */
        @Override
        public boolean ignoreTable(String tableName) {
            assert caffeineCacheManager != null;
            Cache cache = caffeineCacheManager.getCache("FRAMEWORK-CACHE");
            assert cache != null;
            Boolean hasTenantIdField = cache.get(tableName, Boolean.class);
            if (Objects.nonNull(hasTenantIdField)) {
                return hasTenantIdField;
            }
            assert dbSchemaService != null;
            List<GXDBSchemaService.TableField> tableFieldList = dbSchemaService.getTableColumn(tableName);
            for (GXDBSchemaService.TableField tableField : tableFieldList) {
                String name = tableField.getColumnName();
                if (CharSequenceUtil.equalsIgnoreCase(name, getTenantIdColumn())) {
                    cache.put(tableName, Boolean.FALSE);
                    return false;
                }
            }
            cache.put(tableName, Boolean.TRUE);
            return true;
        }
    }
}
