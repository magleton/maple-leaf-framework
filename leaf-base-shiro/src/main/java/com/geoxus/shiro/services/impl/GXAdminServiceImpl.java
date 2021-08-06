package com.geoxus.shiro.services.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.constant.GXTokenConstants;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.shiro.dto.req.GXAdminLoginReqDto;
import com.geoxus.shiro.entities.GXAdminEntity;
import com.geoxus.shiro.mapper.GXAdminMapper;
import com.geoxus.shiro.services.GXAdminService;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class GXAdminServiceImpl extends ServiceImpl<GXAdminMapper, GXAdminEntity> implements GXAdminService<GXAdminEntity> {
    /**
     * 获取当前登录管理员的状态
     *
     * @param adminId 管理员ID
     * @return Dict
     */
    @Override
    public Dict getStatus(long adminId) {
        final HashSet<String> column = CollUtil.newHashSet("id", "status", "username", "phone", "super_admin");
        final Dict condition = Dict.create().set("id", adminId);
        final Dict data = getFieldValueBySQL(GXAdminEntity.class, column, condition, false);
        if (data.size() == 0) {
            return null;
        }
        data.set(GXTokenConstants.ADMIN_ID, adminId);
        return data;
    }

    /**
     * 管理员登录
     *
     * @param loginReqDto 登录信息
     * @return 管理员token
     */
    @Override
    public String login(GXAdminLoginReqDto loginReqDto) {
        final String username = loginReqDto.getUsername();
        final String password = loginReqDto.getPassword();
        String secretPassword = SecureUtil.md5(password);
        final Dict condition = Dict.create()
                .set("username", username)
                .set("password", secretPassword);
        final HashSet<String> column = CollUtil.newHashSet("id", "phone", "status");
        final Dict data = getFieldValueBySQL(GXAdminEntity.class, column, condition, false);
        if (data.size() == 0) {
            throw new GXException("登录信息不存在,请核对之后重试!!");
        }
        final Long adminId = Convert.toLong(data.remove("id"));
        return GXTokenManager.generateAdminToken(adminId, data);
    }
}
