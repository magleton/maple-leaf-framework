package cn.maple.core.datasource.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.maple.core.framework.util.GXCommonUtils;
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
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.HashMap;
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
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // sql性能规范插件
        boolean illegalSQLEnable = Boolean.TRUE.equals(applicationContext.getEnvironment().getProperty("illegal.sql.enable", boolean.class));
        if (illegalSQLEnable) {
            interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }
        // 多租户插件(请在相应的表中新增tenant_id字段)
        boolean tenantEnable = Boolean.TRUE.equals(applicationContext.getEnvironment().getProperty("tenant.enable", boolean.class));
        if (tenantEnable) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new GXTenantLineHandler()));
        }
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
         * 获取租户 ID 值表达式，只支持单个 ID 值
         * <p>
         *
         * @return 租户 ID 值表达式
         */
        @Override
        public Expression getTenantId() {
            return new LongValue(1);
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
            return false;
        }
    }
}
