package com.geoxus.core.common.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * MyBatis公共字段自动填充器
 *
 * @author britton <britton@126.com>
 */
@Slf4j
@Component
public class GXAutoFillMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        final int timestamp = (int) DateUtil.currentSeconds();
        this.setFieldValByName("createdAt", timestamp, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        final int timestamp = (int) DateUtil.currentSeconds();
        this.setFieldValByName("updatedAt", timestamp, metaObject);
    }
}
