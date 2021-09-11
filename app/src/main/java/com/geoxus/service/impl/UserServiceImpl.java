package com.geoxus.service.impl;

import com.geoxus.dao.UserDao;
import com.geoxus.dto.protocol.req.UserReqProtocol;
import com.geoxus.dto.protocol.res.UserResProtocol;
import com.geoxus.entity.UserEntity;
import com.geoxus.mapstruct.UserProtocolMapstruct;
import com.geoxus.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Resource
    private UserProtocolMapstruct userProtocolMapstruct;

    @Override
    public UserResProtocol getUserInfo(Long userId) {
        return userDao.getUserInfo(userId);
    }

    @Override
    public void createOrUpdate(UserReqProtocol userReqProtocol) {
        UserEntity userEntity = userProtocolMapstruct.dtoToEntity(userReqProtocol);
        userDao.save(userEntity);
    }
}
