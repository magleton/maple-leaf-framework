package com.geoxus.service.impl;

import com.geoxus.dao.UserDao;
import com.geoxus.dto.protocol.res.UserResProtocol;
import com.geoxus.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public UserResProtocol getUserInfo(Long userId) {
        return userDao.getUserInfo(userId);
    }
}
