package com.geoxus.order.service;

import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.order.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.order.common.dto.protocol.res.UserResProtocol;
import com.geoxus.order.dao.UserDao;
import com.geoxus.order.entity.UserEntity;
import com.geoxus.order.mapper.UserMapper;

public interface UserService extends GXBusinessService<UserEntity, UserMapper, UserDao> {
    UserResProtocol getUserInfo(Long userId);

    void createOrUpdate(UserReqProtocol userReqProtocol);
}
