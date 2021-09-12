package com.geoxus.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.builder.UserBuilder;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.dto.protocol.res.UserResProtocol;
import com.geoxus.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper extends GXBaseMapper<UserEntity> {
    @SelectProvider(type = UserBuilder.class, method = "getUserInfo")
    UserResProtocol getUserInfo(Long userId);
}
