package com.geoxus.shiro.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.shiro.dao.GXAdminDao;
import com.geoxus.shiro.dto.req.GXAdminLoginReqDto;
import com.geoxus.shiro.entities.GXAdminEntity;
import com.geoxus.shiro.mapper.GXAdminMapper;

public interface GXAdminService extends GXBusinessService<GXAdminEntity, GXAdminMapper, GXAdminDao, Dict> {
    /**
     * 获取当前登录管理员的状态
     *
     * @param adminId 管理员ID
     * @return Dict
     */
    Dict getStatus(long adminId);

    /**
     * 管理员登录
     *
     * @param loginReqDto 登录信息
     * @return 管理员token
     */
    String login(GXAdminLoginReqDto loginReqDto);
}
