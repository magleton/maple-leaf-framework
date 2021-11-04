package cn.maple.shiro.oauth;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXTokenManagerUtils;
import cn.maple.shiro.service.GXShiroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class GXOAuth2Realm extends AuthorizingRealm {
    @Resource
    private GXShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof GXOAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("授权验证权限时调用-->GXOAuth2Realm.doGetAuthorizationInfo() principals : " + principals.getPrimaryPrincipal());
        Dict dict = (Dict) principals.getPrimaryPrincipal();
        Long adminId = Optional.ofNullable(dict.getLong(GXTokenConstant.ADMIN_ID)).orElse(dict.getLong(GXCommonUtils.toCamelCase(GXTokenConstant.ADMIN_ID)));
        // 获取用户权限列表
        Set<String> permsSet = shiroService.getAdminAllPermissions(adminId);
        // 获取用户角色列表
        Set<String> rolesSet = shiroService.getAdminRoles(adminId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        info.addRoles(rolesSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        log.info("用户登录时认证 --> GXOAuth2Realm.doGetAuthenticationInfo() ,  principal = {} : credentials = {}", token.getPrincipal(), token.getCredentials());
        String accessToken = token.getPrincipal().toString();
        // 根据accessToken, 获取token中的值
        Dict dict = GXTokenManagerUtils.decodeAdminToken(accessToken);
        // 判断token是否失效
        if (null == dict || dict.isEmpty()) {
            throw new IncorrectCredentialsException("长时间未操作, 请重新登录~~~");
        }
        // 从TOKEN中获取用户ID
        Long adminId = dict.getLong(GXTokenConstant.ADMIN_ID);
        if (null == adminId) {
            throw new IncorrectCredentialsException("请提供正确的字段");
        }
        // 根据用户ID查询用户信息
        Dict admin = shiroService.getAdminById(adminId);
        // TODO 进行附加处理 可以通过派发事件的方式处理
        shiroService.additionalTreatment(admin);
        // TODO 调用更新token的ttl方法 , 用于维持token的有效时间 可以通过派发事件的方式处理
        shiroService.updateAdminTokenExpirationTime();
        return new SimpleAuthenticationInfo(admin, accessToken, getName());
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        Dict admin = (Dict) principals.getPrimaryPrincipal();
        return shiroService.isSuperAdmin(admin) || super.isPermitted(principals, permission);
    }

    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        Dict admin = (Dict) principals.getPrimaryPrincipal();
        return shiroService.isSuperAdmin(admin) || super.hasRole(principals, roleIdentifier);
    }
}
