package com.geoxus.shiro.oauth;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.constant.GXTokenConstants;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.vo.common.GXBusinessStatusCode;
import com.geoxus.shiro.services.GXShiroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class GXOAuth2Realm extends AuthorizingRealm {
    @Resource
    private GXShiroService gxShiroService;

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
        Long adminId = Optional.ofNullable(dict.getLong(GXTokenConstants.ADMIN_ID)).orElse(dict.getLong(GXCommonUtils.toCamelCase(GXTokenConstants.ADMIN_ID)));
        // 获取用户权限列表
        Set<String> permsSet = gxShiroService.getAdminAllPermissions(adminId);
        // 获取用户角色列表
        Set<String> rolesSet = gxShiroService.getAdminRoles(adminId);
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
        Dict dict = GXTokenManager.decodeAdminToken(accessToken);
        // 判断token是否失效
        if (null == dict || dict.isEmpty()) {
            throw new IncorrectCredentialsException("长时间未操作, 请重新登录~~~");
        }
        // 从TOKEN中获取用户ID
        Long adminId = dict.getLong(GXTokenConstants.ADMIN_ID);
        if (null == adminId) {
            throw new IncorrectCredentialsException("请提供正确的字段");
        }
        // 根据用户ID查询用户信息
        Dict admin = gxShiroService.getAdminById(adminId);
        // 判断账号状态
        Integer userStatus = admin.getInt("status");
        // 用户账户为锁定状态
        if (Objects.isNull(userStatus) || userStatus == GXBusinessStatusCode.LOCKED.getCode()) {
            throw new LockedAccountException(GXBusinessStatusCode.LOCKED.getMsg());
        }
        return new SimpleAuthenticationInfo(admin, accessToken, getName());
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        Dict admin = (Dict) principals.getPrimaryPrincipal();
        return gxShiroService.isSuperAdmin(admin) || super.isPermitted(principals, permission);
    }

    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        Dict admin = (Dict) principals.getPrimaryPrincipal();
        return gxShiroService.isSuperAdmin(admin) || super.hasRole(principals, roleIdentifier);
    }
}
