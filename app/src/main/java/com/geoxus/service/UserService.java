package com.geoxus.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.dao.UserDao;
import com.geoxus.dto.protocol.req.UserReqProtocol;
import com.geoxus.dto.protocol.res.UserResProtocol;
import com.geoxus.entity.UserEntity;
import com.geoxus.mapper.UserMapper;

public interface UserService extends GXBusinessService<UserEntity, UserMapper, UserDao> {
    UserResProtocol getUserInfo(Long userId);

    void createOrUpdate(UserReqProtocol userReqProtocol);
}
