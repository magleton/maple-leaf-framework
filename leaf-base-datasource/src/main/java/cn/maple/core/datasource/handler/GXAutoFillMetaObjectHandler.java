package cn.maple.core.datasource.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.service.GXMyBatisAutoFillMetaObjectService;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
        String createdBy = (String) metaObject.getValue("createdBy");
        if (CharSequenceUtil.isBlank(createdBy)) {
            createdBy = "unknown";
            GXMyBatisAutoFillMetaObjectService myBatisAutoFillMetaObjectService = GXSpringContextUtils.getBean(GXMyBatisAutoFillMetaObjectService.class);
            if (Objects.nonNull(myBatisAutoFillMetaObjectService)) {
                createdBy = myBatisAutoFillMetaObjectService.getCreatedBy();
            }
            this.setFieldValByName("createdBy", createdBy, metaObject);
        }
        final Integer timestamp = Math.toIntExact(DateUtil.currentSeconds());
        this.setFieldValByName("createdAt", timestamp, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String updatedBy = (String) metaObject.getValue("updatedBy");
        if (CharSequenceUtil.isBlank(updatedBy)) {
            updatedBy = "unknown";
            GXMyBatisAutoFillMetaObjectService myBatisAutoFillMetaObjectService = GXSpringContextUtils.getBean(GXMyBatisAutoFillMetaObjectService.class);
            if (Objects.nonNull(myBatisAutoFillMetaObjectService)) {
                updatedBy = myBatisAutoFillMetaObjectService.getUpdatedBy();
            }
            this.setFieldValByName("updatedBy", updatedBy, metaObject);
        }
        final Integer timestamp = Math.toIntExact(DateUtil.currentSeconds());
        this.setFieldValByName("updatedAt", timestamp, metaObject);
    }
}
