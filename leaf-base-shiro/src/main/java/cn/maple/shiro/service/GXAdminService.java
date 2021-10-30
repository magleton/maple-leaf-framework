package cn.maple.shiro.service;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.service.GXDBBaseService;
import cn.maple.core.framework.pojo.GXBusinessStatusCode;
import cn.maple.shiro.dao.GXAdminDao;
import cn.maple.shiro.dto.req.GXAdminLoginReqDto;
import cn.maple.shiro.dto.res.GXAdminResDto;
import cn.maple.shiro.entities.GXAdminEntity;
import cn.maple.shiro.mapper.GXAdminMapper;
import org.apache.shiro.authc.LockedAccountException;

import java.util.Objects;

public interface GXAdminService extends
        GXDBBaseService<GXAdminEntity, GXAdminMapper, GXAdminDao, GXAdminResDto> {
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

    /**
     * 更新管理员token的过期时间
     */
    default void updateAdminTokenExpirationTime() {
    }

    /**
     * 对token中的数据进行业务处理
     *
     * @param data 管理员数据
     */
    default void additionalTreatment(Dict data) {
        // 判断账号状态
        Integer userStatus = data.getInt("status");
        // 用户账户为锁定状态
        if (Objects.isNull(userStatus) || userStatus == GXBusinessStatusCode.LOCKED.getCode()) {
            throw new LockedAccountException(GXBusinessStatusCode.LOCKED.getMsg());
        }
    }
}
