package com.geoxus.shiro.config;

import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import com.geoxus.shiro.oauth.GXOAuth2Filter;
import com.geoxus.shiro.oauth.GXOAuth2Realm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.*;

@Component
@Slf4j
public class GXShiRoConfig {
    public GXShiRoConfig() {
        log.info("ShiRoConfig init ......");
    }

    @Bean("sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    /**
     * 安全管理器的配置
     *
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(GXOAuth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * ShiRo过滤器配置
     * <p>
     * 如果Controller使用了@RequiresPermissions注解来设置了权限验证，
     * 则filterChainDefinitionMap必须包含oauth2过滤器
     * 否则会报"This subject is anonymous"这类的错误
     *
     * @param securityManager 安全管理器
     * @return ShiroFilterFactoryBean
     * @author britton <britton@126.com>
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, @Autowired ShiroCustomerConfig shiroCustomerConfig) {
        log.info("ShiRoConfiguration.shiRoFilter() ......");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new GXOAuth2Filter());
        shiroFilterFactoryBean.setFilters(filters);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        for (Map<String, String> map : shiroCustomerConfig.getConfig()) {
            filterChainDefinitionMap.putIfAbsent(map.get("key"), map.get("value"));
        }
        filterChainDefinitionMap.put("/**", "oauth2");   // 不能删除
        filterChainDefinitionMap.put("/logout", "logout");
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 自定义缓存
     *
     * @return CacheManager
     */
    @Bean
    protected AbstractCacheManager shiRoCacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 开启@RequirePermission注解的配置，要结合DefaultAdvisorAutoProxyCreator一起使用，或者导入aop的依赖
     *
     * @param securityManager 安全管理器
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Data
    @Component
    @PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/shiro-config.yml"},
            factory = GXYamlPropertySourceFactory.class,
            ignoreResourceNotFound = true)
    @ConfigurationProperties(prefix = "shiro")
    static class ShiroCustomerConfig {
        private List<Map<String, String>> config = new ArrayList<>();
    }
}
