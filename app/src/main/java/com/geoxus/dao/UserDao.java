package com.geoxus.dao;

import com.geoxus.core.common.dao.GXBaseDao;
import com.geoxus.dto.protocol.res.UserResProtocol;
import com.geoxus.entity.UserEntity;
import com.geoxus.mapper.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GXBaseDao<UserMapper, UserEntity> {
    public UserResProtocol getUserInfo(Long userId) {
        return baseMapper.getUserInfo(userId);
    }
}
