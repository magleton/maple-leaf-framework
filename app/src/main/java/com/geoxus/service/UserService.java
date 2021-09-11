package com.geoxus.service;

import com.geoxus.dto.protocol.req.UserReqProtocol;
import com.geoxus.dto.protocol.res.UserResProtocol;

public interface UserService {
    UserResProtocol getUserInfo(Long userId);

    void createOrUpdate(UserReqProtocol userReqProtocol);
}
