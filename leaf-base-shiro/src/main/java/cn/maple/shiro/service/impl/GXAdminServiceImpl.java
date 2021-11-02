package cn.maple.shiro.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SecureUtil;
import cn.maple.core.datasource.service.impl.GXDBBaseServiceImpl;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXTokenManagerUtils;
import cn.maple.shiro.dao.GXAdminDao;
import cn.maple.shiro.dto.req.GXAdminLoginReqDto;
import cn.maple.shiro.dto.res.GXAdminResDto;
import cn.maple.shiro.entities.GXAdminEntity;
import cn.maple.shiro.mapper.GXAdminMapper;
import cn.maple.shiro.service.GXAdminService;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class GXAdminServiceImpl
        extends GXDBBaseServiceImpl<GXAdminEntity, GXAdminMapper, GXAdminDao, GXAdminResDto>
        implements GXAdminService {
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
        data.set(GXTokenConstant.ADMIN_ID, adminId);
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
        GXAdminEntity gxAdminEntity = new GXAdminEntity();
        gxAdminEntity.setExt(Dict.create().set("aaaa", "aaaa"));
        gxAdminEntity.setUsername("aaammm");
        baseDao.saveOrUpdate(gxAdminEntity);
        System.out.println("aaaa : " + gxAdminEntity.getId());
        final String username = loginReqDto.getUsername();
        final String password = loginReqDto.getPassword();
        String secretPassword = SecureUtil.md5(password);
        final Dict condition = Dict.create()
                .set("username", username)
                .set("password", secretPassword);
        final HashSet<String> column = CollUtil.newHashSet("id", "phone", "status");
        final Dict data = getFieldValueBySQL(GXAdminEntity.class, column, condition, false);
        if (data.size() == 0) {
            throw new GXBusinessException("登录信息不存在,请核对之后重试!!");
        }
        final Long adminId = Convert.toLong(data.remove("id"));
        return GXTokenManagerUtils.generateAdminToken(adminId, data);
    }
}
