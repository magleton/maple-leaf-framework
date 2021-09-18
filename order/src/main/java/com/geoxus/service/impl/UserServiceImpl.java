package com.geoxus.service.impl;

import com.geoxus.core.common.service.impl.GXBusinessServiceImpl;
import com.geoxus.dao.UserDao;
import com.geoxus.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.common.dto.protocol.res.UserResProtocol;
import com.geoxus.entity.UserEntity;
import com.geoxus.mapper.UserMapper;
import com.geoxus.common.mapstruct.UserProtocolMapstruct;
import com.geoxus.service.UserService;
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
