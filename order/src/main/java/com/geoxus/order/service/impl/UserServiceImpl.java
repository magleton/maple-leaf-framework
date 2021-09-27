package com.geoxus.order.service.impl;

import com.geoxus.core.framework.service.impl.GXBusinessServiceImpl;
import com.geoxus.order.dto.protocol.req.UserReqProtocol;
import com.geoxus.order.dto.protocol.res.UserResProtocol;
import com.geoxus.order.mapstruct.UserProtocolMapstruct;
import com.geoxus.order.dao.UserDao;
import com.geoxus.order.entity.UserEntity;
import com.geoxus.order.mapper.UserMapper;
import com.geoxus.order.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends GXBusinessServiceImpl<UserEntity, UserMapper, UserDao> implements UserService {
    @Resource
    private UserProtocolMapstruct userProtocolMapstruct;

    @Override
    public UserResProtocol getUserInfo(Long userId) {
        return baseDao.getUserInfo(userId);
    }

    @Override
    public void createOrUpdate(UserReqProtocol userReqProtocol) {
        UserEntity userEntity = userProtocolMapstruct.dtoToEntity(userReqProtocol);
        baseDao.save(userEntity);
    }
}
