package com.geoxus.order.dao;

import com.geoxus.core.datasource.dao.GXBaseDao;
import com.geoxus.order.entity.UserEntity;
import com.geoxus.order.mapper.UserMapper;
import com.geoxus.order.dto.protocol.res.UserResProtocol;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GXBaseDao<UserMapper, UserEntity> {
    public UserResProtocol getUserInfo(Long userId) {
        return baseMapper.getUserInfo(userId);
    }
}
