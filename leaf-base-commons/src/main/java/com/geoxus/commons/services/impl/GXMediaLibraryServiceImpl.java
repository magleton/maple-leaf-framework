package com.geoxus.commons.services.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.commons.config.GXUploadConfig;
import com.geoxus.commons.entities.GXMediaLibraryEntity;
import com.geoxus.commons.mappers.GXMediaLibraryMapper;
import com.geoxus.commons.services.GXMediaLibraryService;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.util.GXUploadUtils;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.framework.service.GXCoreModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "mediaLibraryService")
@GXDataSourceAnnotation("framework")
public class GXMediaLibraryServiceImpl extends ServiceImpl<GXMediaLibraryMapper, GXMediaLibraryEntity> implements GXMediaLibraryService {
    @Autowired
    private GXUploadConfig gxUploadConfig;

    @Autowired
    private GXCoreModelService coreModelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(Dict dict) {
        final GXMediaLibraryEntity entity = dict.toBean(GXMediaLibraryEntity.class);
        save(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOwner(long targetId, long coreModelId, List<JSONObject> mediaList, Dict condition) {
        if (mediaList.isEmpty()) {
            return;
        }
        final ArrayList<GXMediaLibraryEntity> newMediaList = new ArrayList<>();
        QueryWrapper<GXMediaLibraryEntity> oldConditionQuery = new QueryWrapper<>();
        Dict oldCondition = Dict.create().set("target_id", targetId).set(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId);
        if (null != condition.getObj("resource_type")) {
            oldCondition.set("resource_type", condition.getStr("resource_type"));
        }
        final List<GXMediaLibraryEntity> oldMediaList = list(oldConditionQuery.allEq(oldCondition));
        Set<Integer> saveOldData = CollUtil.newHashSet();
        for (JSONObject media : mediaList) {
            Integer mediaId = media.getInt("id");
            if (null != mediaId) {
                targetId = media.getLong("target_id", targetId);
                final long itemCoreModelId = media.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, coreModelId);
                final String resourceType = media.getStr("resource_type", "defaultResourceType");
                final GXMediaLibraryEntity entity = getOne(new QueryWrapper<GXMediaLibraryEntity>().eq("id", mediaId));
                String customProperties = media.getStr("custom_properties", "{}");
                Integer updateOld = media.getInt("update_old");
                if (null != updateOld) {
                    saveOldData.add(mediaId);
                    if (JSONUtil.isJson(customProperties)) {
                        Dict bean = JSONUtil.toBean(customProperties, Dict.class);
                        bean.put("update_old", updateOld);
                        customProperties = JSONUtil.toJsonStr(bean);
                    }
                }
                if (null != entity) {
                    entity.setTargetId(targetId);
                    entity.setModelType(coreModelService.getModelTypeByModelId(itemCoreModelId, "defaultModelType"));
                    entity.setCoreModelId(itemCoreModelId);
                    entity.setCustomProperties(customProperties);
                    entity.setResourceType(resourceType);
                    newMediaList.add(entity);
                }
            }
        }
        List<GXMediaLibraryEntity> deleteMedia = CollUtil.filter(oldMediaList,
                (GXMediaLibraryEntity t) -> !saveOldData.contains(t.getId()) && !newMediaList.contains(t));
        if (!deleteMedia.isEmpty()) {
            // TODO : 此处可以加入删除策略
            // TODO : 例如 : 软删除  硬删除等...
            removeByIds(deleteMedia.stream().map(GXMediaLibraryEntity::getId).collect(Collectors.toList()));
        }
        if (!newMediaList.isEmpty()) {
            updateBatchById(newMediaList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GXMediaLibraryEntity saveFileInfo(MultipartFile file, Dict param) {
        String filePath = gxUploadConfig.getDepositPath().trim();
        try {
            String fileName = GXUploadUtils.singleUpload(file, filePath);
            GXMediaLibraryEntity entity = new GXMediaLibraryEntity();
            entity.setSize(file.getSize());
            entity.setFileName(fileName);
            entity.setDisk(filePath);
            entity.setMimeType(file.getContentType());
            entity.setName(file.getOriginalFilename());
            entity.setFilePath(fileName);
            entity.setCollectionName((String) param.getOrDefault("collection_name", "default"));
            entity.setResourceType((String) param.getOrDefault("resource_type", "defaultResourceType"));
            entity.setModelType((String) param.getOrDefault("model_type", "defaultModelType"));
            entity.setTargetId((Long) param.getOrDefault("object_id", 0L));
            entity.setCoreModelId((Long) param.getOrDefault(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME, 0L));
            entity.setCustomProperties((String) param.getOrDefault("custom_properties", "{}"));
            save(entity);
            return entity;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new GXException("文件上传失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCondition(Dict param) {
        return baseMapper.deleteByCondition(param);
    }

    @Override
    public List<Dict> getMediaByCondition(Dict param) {
        return baseMapper.getMediaByCondition(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOldFile(Dict param) {
        Long objectId = Optional.ofNullable(param.getLong("object_id")).orElse(0L);
        Long coreModelId = Optional.ofNullable(param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME)).orElse(0L);
        Dict condition = Convert.convert(Dict.class, Optional.ofNullable(param.getObj("condition")).orElse(Dict.create()));
        List<JSONObject> objectList = Convert.convert(new TypeReference<List<JSONObject>>() {
        }, Optional.ofNullable(param.getObj("data")).orElse(Collections.emptyList()));
        updateOwner(objectId, coreModelId, objectList, condition);
    }
}
