package com.geoxus.order.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.framework.mapper.GXBaseMapper;
import com.geoxus.order.builder.UserBuilder;
import com.geoxus.order.dto.protocol.res.UserResProtocol;
import com.geoxus.order.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper extends GXBaseMapper<UserEntity> {
    @SelectProvider(type = UserBuilder.class, method = "getUserInfo")
    UserResProtocol getUserInfo(Long userId);

    @SelectProvider(type = UserBuilder.class, method = "listOrSearchPage")
    List<UserResProtocol> listOrSearchPage(IPage<UserResProtocol> page, Dict param);
}
