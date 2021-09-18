package com.geoxus.service;

import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.dao.UserDao;
import com.geoxus.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.common.dto.protocol.res.UserResProtocol;
import com.geoxus.entity.UserEntity;
import com.geoxus.mapper.UserMapper;

public interface UserService extends GXBusinessService<UserEntity, UserMapper, UserDao> {
    UserResProtocol getUserInfo(Long userId);

    void createOrUpdate(UserReqProtocol userReqProtocol);
}
